package com.elhady.lafyuu.core.database.repository

import com.elhady.lafyuu.core.database.model.FavoriteItem
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<FavoriteItem>>
    fun observeFavoriteIds(): Flow<List<String>>
    fun isFavorite(productId: String): Flow<Boolean>
    suspend fun addFavorite(item: FavoriteItem)
    suspend fun removeFavorite(productId: String)
    suspend fun clearFavorites()
}
