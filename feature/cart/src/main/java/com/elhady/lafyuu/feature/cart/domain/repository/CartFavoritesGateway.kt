package com.elhady.lafyuu.feature.cart.domain.repository

import com.elhady.lafyuu.feature.cart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartFavoritesGateway {
    fun observeFavoriteProductIds(): Flow<Set<String>>
    suspend fun addFavorite(item: CartItem)
    suspend fun removeFavorite(productId: String)
}