package com.elhady.lafyuu.feature.auth.data.remote

import com.elhady.lafyuu.core.network.auth.RefreshTokenResult
import com.elhady.lafyuu.core.network.auth.TokenRemoteDataSource
import com.elhady.lafyuu.feature.auth.data.remote.model.RefreshTokenRequest
import javax.inject.Inject

class TokenRemoteDataSourceImpl @Inject constructor(
    private val refreshApi: RefreshApi
) : TokenRemoteDataSource {

    override suspend fun refreshToken(refreshToken: String): RefreshTokenResult {
        return try {
            val response = refreshApi.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                val body = response.body()!!
                RefreshTokenResult.Success(
                    accessToken = body.accessToken,
                    refreshToken = body.refreshToken
                )
            } else {
                RefreshTokenResult.Failure
            }
        } catch (e: Exception) {
            RefreshTokenResult.Failure
        }
    }
}
