package com.elhady.lafyuu.feature.cart.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getCart(): AppResult<Cart>
    fun observeCart(): Flow<Cart?>
    suspend fun updateItemQuantity(itemId: String, quantity: Int): AppResult<Unit>
    suspend fun decrementItem(itemId: String, quantity: Int): AppResult<Unit>
    suspend fun removeItem(itemId: String): AppResult<Unit>
    suspend fun applyCoupon(couponCode: String): AppResult<Cart>
}
