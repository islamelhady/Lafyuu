package com.elhady.lafyuu.feature.auth.data.remote

import com.elhady.lafyuu.core.network.auth.RefreshTokenResult
import com.elhady.lafyuu.core.network.auth.TokenRemoteDataSource
import com.elhady.lafyuu.feature.auth.data.remote.model.RefreshTokenRequest
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class TokenRemoteDataSourceImpl @Inject constructor(
    private val refreshApi: RefreshApi
) : TokenRemoteDataSource {

    override suspend fun refreshToken(
        refreshToken: String
    ): RefreshTokenResult {
        return try {
            val response = refreshApi.refreshToken(
                RefreshTokenRequest(refreshToken)
            )

            if (response.isSuccessful) {
                val body = response.body()
                    ?: return RefreshTokenResult.TemporaryFailure

                if (
                    body.accessToken.isBlank() ||
                    body.refreshToken.isBlank()
                ) {
                    return RefreshTokenResult.TemporaryFailure
                }

                RefreshTokenResult.Success(
                    accessToken = body.accessToken,
                    refreshToken = body.refreshToken
                )
            } else {
                when (response.code()) {
                    // Assumption: backend uses 401 for an invalid refresh token.
                    401 -> RefreshTokenResult.Rejected

                    // Other HTTP failures are not sufficient evidence
                    // that the local session is invalid.
                    else -> RefreshTokenResult.TemporaryFailure
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            RefreshTokenResult.TemporaryFailure
        } catch (e: HttpException) {
            RefreshTokenResult.TemporaryFailure
        } catch (e: Exception) {
            // Never interpret an unexpected exception as proof that
            // the refresh token has been rejected.
            RefreshTokenResult.TemporaryFailure
        }
    }
}
