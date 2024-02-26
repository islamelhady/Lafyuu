package com.elhady.lafyuu.core.datastore

import com.elhady.lafyuu.core.network.auth.TokenProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreTokenProvider @Inject constructor(
    private val shoppeDataStore: ShoppeDataStore
) : TokenProvider {
    override fun getAccessToken(): Flow<String?> = shoppeDataStore.accessToken
    
    override fun getRefreshToken(): Flow<String?> = shoppeDataStore.refreshToken

    override suspend fun updateTokens(accessToken: String, refreshToken: String) {
        shoppeDataStore.saveTokens(accessToken, refreshToken)
    }

    override suspend fun clearSession() {
        shoppeDataStore.clearSession()
    }
}
