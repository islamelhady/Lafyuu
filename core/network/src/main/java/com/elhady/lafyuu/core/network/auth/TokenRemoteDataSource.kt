package com.elhady.lafyuu.core.network.auth


sealed class RefreshTokenResult {
    data class Success(val accessToken: String, val refreshToken: String) : RefreshTokenResult()
    data object Rejected : RefreshTokenResult()
    data object TemporaryFailure : RefreshTokenResult()
}

interface TokenRemoteDataSource {
    suspend fun refreshToken(refreshToken: String): RefreshTokenResult
}
