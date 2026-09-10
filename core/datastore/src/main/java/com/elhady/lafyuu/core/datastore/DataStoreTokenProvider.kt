package com.elhady.lafyuu.core.datastore

import com.elhady.lafyuu.core.network.auth.TokenProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreTokenProvider @Inject constructor(
    private val lafyuuDataStore: LafyuuDataStore
) : TokenProvider {
    override fun getAccessToken(): Flow<String?> = lafyuuDataStore.accessToken
    
    override fun getRefreshToken(): Flow<String?> = lafyuuDataStore.refreshToken

    override suspend fun updateTokens(accessToken: String, refreshToken: String) {
        lafyuuDataStore.saveTokens(accessToken, refreshToken)
    }

    override suspend fun clearSession() {
        lafyuuDataStore.clearSession()
    }
}
