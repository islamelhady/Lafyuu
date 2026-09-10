package com.elhady.lafyuu.core.network.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class DefaultTokenProvider @Inject constructor() : TokenProvider {
    override fun getAccessToken(): Flow<String?> = flowOf(null)
    
    override fun getRefreshToken(): Flow<String?> = flowOf(null)

    override suspend fun updateTokens(accessToken: String, refreshToken: String) {
        // No-op
    }

    override suspend fun clearSession() {
        // No-op
    }
}
