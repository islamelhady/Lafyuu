package com.elhady.lafyuu.core.network.auth

import kotlinx.coroutines.flow.Flow

interface TokenProvider {
    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    suspend fun updateTokens(accessToken: String, refreshToken: String)
    suspend fun clearSession()
}
