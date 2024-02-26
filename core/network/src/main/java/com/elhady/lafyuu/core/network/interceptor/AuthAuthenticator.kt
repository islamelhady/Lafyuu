package com.elhady.lafyuu.core.network.interceptor

import com.elhady.lafyuu.core.network.auth.RefreshTokenResult
import com.elhady.lafyuu.core.network.auth.TokenProvider
import com.elhady.lafyuu.core.network.auth.TokenRemoteDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val tokenRemoteDataSource: TokenRemoteDataSource
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        // 0. Explicit retry protection: only attempt refresh/retry once per original request.
        if (response.priorResponse != null) {
            return null
        }

        // 1. Get the access token used in the failed request
        val currentAccessToken = response.request.header("Authorization")?.removePrefix("Bearer ")

        return runBlocking {
            mutex.withLock {
                // 2. Check if the token has already been refreshed by another concurrent request
                val latestAccessToken = tokenProvider.getAccessToken().firstOrNull()

                if (!latestAccessToken.isNullOrBlank() && latestAccessToken != currentAccessToken) {
                    // Token was already refreshed, retry with the latest one
                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer $latestAccessToken")
                        .build()
                }

                // 3. Obtain refresh token
                val refreshToken = tokenProvider.getRefreshToken().firstOrNull()
                if (refreshToken.isNullOrBlank()) {
                    tokenProvider.clearSession()
                    return@runBlocking null
                }

                // 4. Perform refresh
                when (val result = tokenRemoteDataSource.refreshToken(refreshToken)) {
                    is RefreshTokenResult.Success -> {
                        // 5. Update tokens atomically
                        tokenProvider.updateTokens(result.accessToken, result.refreshToken)

                        // 6. Retry original request with new token
                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${result.accessToken}")
                            .build()
                    }
                    RefreshTokenResult.Failure -> {
                        // 7. Refresh failed, clear session
                        tokenProvider.clearSession()
                        null
                    }
                }
            }
        }
    }
}
