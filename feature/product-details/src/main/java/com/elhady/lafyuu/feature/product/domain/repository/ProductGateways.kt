package com.elhady.lafyuu.feature.product.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import kotlinx.coroutines.flow.Flow

interface ProductCartGateway {
    fun observeCartItemId(productId: String): Flow<String?>
    suspend fun removeCartItem(cartItemId: String): AppResult<Unit>
    suspend fun refresh()
}

interface ProductFavoriteGateway {
    fun isFavorite(productId: String): Flow<Boolean>
    suspend fun addFavorite(product: ProductDetails)
    suspend fun removeFavorite(productId: String)
}