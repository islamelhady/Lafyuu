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

    override suspend fun getCart(): AppResult<Cart> = mutex.withLock {
        safeApiCaller.call { cartApi.getCart().toDomain() }
            .also { if (it is AppResult.Success) _cartFlow.value = it.data }
    }

    override suspend fun updateItemQuantity(itemId: String, quantity: Int): AppResult<Unit> =
        mutex.withLock {
            safeApiCaller.call {
                cartApi.updateCartItem(id = itemId, request = UpdateItemRequestDto(quantity))
            }
        }

    override suspend fun decrementItem(itemId: String, quantity: Int): AppResult<Unit> =
        mutex.withLock {
            safeApiCaller.call {
                cartApi.decrementCartItem(
                    request = DecrementItemRequestDto(itemId, quantity)
                )
            }
        }

    override suspend fun removeItem(itemId: String): AppResult<Unit> = mutex.withLock {
        safeApiCaller.call { cartApi.deleteCartItem(itemId) }
    }

    override suspend fun applyCoupon(couponCode: String): AppResult<Cart> = mutex.withLock {
        safeApiCaller.call {
            cartApi.applyCoupon(request = ApplyCouponRequestDto(couponCode))
                .toDomain(fallbackCartId = _cartFlow.value?.cartId.orEmpty())
        }.also { if (it is AppResult.Success) _cartFlow.value = it.data }
    }
}