package com.elhady.lafyuu.feature.favorites.domain.repository

import com.elhady.lafyuu.feature.favorites.domain.model.FavoriteProduct
import kotlinx.coroutines.flow.Flow

interface FavoriteProductsRepository {
    fun observeFavorites(): Flow<List<FavoriteProduct>>
    suspend fun removeFavorite(productId: String)
}