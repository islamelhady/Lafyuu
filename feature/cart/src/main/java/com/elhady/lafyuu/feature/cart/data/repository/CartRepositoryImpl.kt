package com.elhady.lafyuu.feature.cart.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.cart.data.mapper.toDomain
import com.elhady.lafyuu.feature.cart.data.remote.CartApi
import com.elhady.lafyuu.feature.cart.data.remote.model.ApplyCouponRequestDto
import com.elhady.lafyuu.feature.cart.data.remote.model.DecrementItemRequestDto
import com.elhady.lafyuu.feature.cart.data.remote.model.UpdateItemRequestDto
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartApi: CartApi,
    private val safeApiCaller: SafeApiCaller
) : CartRepository {

    private val mutex = Mutex()
    private val _cartFlow = MutableStateFlow<Cart?>(null)

    override fun observeCart(): Flow<Cart?> = _cartFlow.asStateFlow()

    override suspend fun getCart(): AppResult<Cart> = mutex.withLock { fetchCartUnlocked() }

    override suspend fun updateItemQuantity(itemId: String, quantity: Int): AppResult<Unit> =
        mutex.withLock {
            safeApiCaller.call {
                cartApi.updateCartItem(id = itemId, request = UpdateItemRequestDto(quantity))
            }.thenRefreshCart()
        }

    override suspend fun decrementItem(itemId: String, quantity: Int): AppResult<Unit> =
        mutex.withLock {
            safeApiCaller.call {
                cartApi.decrementCartItem(request = DecrementItemRequestDto(itemId, quantity))
            }.thenRefreshCart()
        }

    override suspend fun removeItem(itemId: String): AppResult<Unit> = mutex.withLock {
        safeApiCaller.call { cartApi.deleteCartItem(itemId) }.thenRefreshCart()
    }

    override suspend fun applyCoupon(couponCode: String): AppResult<Cart> = mutex.withLock {
        val current = _cartFlow.value
        safeApiCaller.call {
            cartApi.applyCoupon(request = ApplyCouponRequestDto(couponCode)).toDomain(
                fallbackCartId = current?.cartId.orEmpty(),
                // the coupon response has totals only: keep the items we already have
                items = current?.items.orEmpty()
            )
        }.also { if (it is AppResult.Success) _cartFlow.value = it.data }
    }

    private suspend fun fetchCartUnlocked(): AppResult<Cart> =
        safeApiCaller.call { cartApi.getCart().toDomain() }
            .also { if (it is AppResult.Success) _cartFlow.value = it.data }

    /**
     * After a successful mutation the cache is re-synced with the server so every
     * observer (cart screen, tab badge, product screen) sees the same data.
     * A failed refresh doesn't fail the mutation, it already succeeded.
     */
    private suspend fun <T> AppResult<T>.thenRefreshCart(): AppResult<T> {
        if (this is AppResult.Success) fetchCartUnlocked()
        return this
    }
}