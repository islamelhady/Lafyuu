package com.elhady.lafyuu.feature.product.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import kotlinx.coroutines.flow.Flow

interface ProductCartGateway {
    fun observeCartItemId(productId: String): Flow<String?>
    suspend fun removeCartItem(cartItemId: String): AppResult<Unit>
    suspend fun refresh()
}

interface ProductFavoriteGateway {
    fun isFavorite(productId: String): Flow<Boolean>
    suspend fun setFavorite(productId: String, isFavorite: Boolean)
}