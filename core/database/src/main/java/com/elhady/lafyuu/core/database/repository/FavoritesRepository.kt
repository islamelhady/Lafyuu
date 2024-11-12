package com.elhady.lafyuu.core.database.repository

import com.elhady.lafyuu.core.database.model.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<Favorite>>
    fun isFavorite(productId: String): Flow<Boolean>
    suspend fun addFavorite(item: Favorite)
    suspend fun removeFavorite(productId: String)
    suspend fun clearFavorites()
}
