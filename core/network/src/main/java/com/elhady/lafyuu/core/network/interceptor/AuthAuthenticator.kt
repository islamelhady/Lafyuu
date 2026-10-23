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

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {
        // Prevent retry loops.
        if (response.priorResponse != null) {
            return null
        }

        val currentAccessToken = response.request
            .header("Authorization")
            ?.removePrefix("Bearer ")

        return runBlocking {
            mutex.withLock {
                // Another request may already have refreshed the token.
                val latestAccessToken =
                    tokenProvider.getAccessToken().firstOrNull()

                if (
                    !latestAccessToken.isNullOrBlank() &&
                    latestAccessToken != currentAccessToken
                ) {
                    return@withLock response.request.newBuilder()
                        .header(
                            "Authorization",
                            "Bearer $latestAccessToken"
                        )
                        .build()
                }

                val refreshToken =
                    tokenProvider.getRefreshToken().firstOrNull()

                if (refreshToken.isNullOrBlank()) {
                    // There is no refresh credential to recover the session.
                    tokenProvider.clearSession()
                    return@withLock null
                }

                when (
                    val result = tokenRemoteDataSource.refreshToken(
                        refreshToken
                    )
                ) {
                    is RefreshTokenResult.Success -> {
                        tokenProvider.updateTokens(
                            accessToken = result.accessToken,
                            refreshToken = result.refreshToken
                        )

                        response.request.newBuilder()
                            .header(
                                "Authorization",
                                "Bearer ${result.accessToken}"
                            )
                            .build()
                    }

                    RefreshTokenResult.Rejected -> {
                        // Clear only when the refresh credential is
                        // explicitly rejected by the server.
                        tokenProvider.clearSession()
                        null
                    }

                    RefreshTokenResult.TemporaryFailure -> {
                        // Preserve the session. The original request
                        // remains unauthorized, but refresh can be retried
                        // by a later request.
                        null
                    }
                }
            }
        }
    }
}