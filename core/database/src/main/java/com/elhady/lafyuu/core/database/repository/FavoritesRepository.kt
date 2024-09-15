package com.elhady.lafyuu.core.database.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavoriteIds(): Flow<List<String>>
    fun isFavorite(productId: String): Flow<Boolean>
    suspend fun addFavorite(productId: String)
    suspend fun removeFavorite(productId: String)
    suspend fun clearFavorites()
}
