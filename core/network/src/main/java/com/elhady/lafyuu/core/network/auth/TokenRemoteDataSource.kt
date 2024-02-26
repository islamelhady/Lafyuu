package com.elhady.lafyuu.core.network.auth


sealed class RefreshTokenResult {
    data class Success(val accessToken: String, val refreshToken: String) : RefreshTokenResult()
    data object Failure : RefreshTokenResult()
}

interface TokenRemoteDataSource {
    suspend fun refreshToken(refreshToken: String): RefreshTokenResult
}
