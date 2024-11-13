package com.elhady.lafyuu.feature.cart.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.cart.data.remote.CartApi
import com.elhady.lafyuu.feature.cart.data.remote.model.ApplyCouponResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.CartItemResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.GetCartResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CartRepositoryImplTest {

    private val cartApi: CartApi = mockk()
    private val safeApiCaller: SafeApiCaller = mockk {
        coEvery { call<Any>(any()) } coAnswers {
            AppResult.Success((firstArg() as suspend () -> Any).invoke())
        }
    }

    private lateinit var repository: CartRepositoryImpl

    @Before
    fun setUp() {
        repository = CartRepositoryImpl(cartApi, safeApiCaller)
    }

    private fun cartDto(vararg itemIds: String) = GetCartResponseDto(
        cartId = "c-1",
        cartItems = itemIds.map { CartItemResponseDto(itemId = it, productId = "p-$it", quantity = 1) }
    )

    @Test
    fun `getCart returns success and publishes the cart`() = runTest {
        coEvery { cartApi.getCart() } returns cartDto()

        val result = repository.getCart()

        assertTrue(result is AppResult.Success)
        assertEquals("c-1", (result as AppResult.Success).data.cartId)
        assertEquals("c-1", repository.observeCart().first()?.cartId)
    }

    @Test(expected = CancellationException::class)
    fun `getCart rethrows CancellationException`() = runTest {
        coEvery { cartApi.getCart() } throws CancellationException("Coroutine cancelled")

        repository.getCart()
    }

    @Test
    fun `updateItemQuantity refreshes the cart after success`() = runTest {
        coEvery { cartApi.updateCartItem(any(), any()) } returns Unit
        coEvery { cartApi.getCart() } returns cartDto("i-1")

        val result = repository.updateItemQuantity("i-1", 2)

        assertTrue(result is AppResult.Success)
        coVerify(exactly = 1) { cartApi.getCart() }
        assertEquals(1, repository.observeCart().first()?.items?.size)
    }

    @Test(expected = CancellationException::class)
    fun `updateItemQuantity rethrows CancellationException from refresh`() = runTest {
        coEvery { cartApi.updateCartItem(any(), any()) } returns Unit
        coEvery { cartApi.getCart() } throws CancellationException("Coroutine cancelled")

        repository.updateItemQuantity("i-1", 2)
    }

    @Test
    fun `removeItem refreshes the cart after success`() = runTest {
        coEvery { cartApi.deleteCartItem(any()) } returns Unit
        coEvery { cartApi.getCart() } returns cartDto()

        val result = repository.removeItem("i-1")

        assertTrue(result is AppResult.Success)
        assertTrue(repository.observeCart().first()?.items.orEmpty().isEmpty())
    }

    @Test
    fun `applyCoupon returns totals and keeps the cached items`() = runTest {
        coEvery { cartApi.getCart() } returns cartDto("i-1")
        repository.getCart()
        coEvery { cartApi.applyCoupon(any()) } returns
                ApplyCouponResponseDto(cartId = "c-1", finalTotal = 50.0)

        val result = repository.applyCoupon("SUMMER")

        assertTrue(result is AppResult.Success)
        val cart = (result as AppResult.Success).data
        assertEquals(50.0, cart.finalTotal!!, 0.001)
        assertEquals(1, cart.items.size)
        assertEquals(1, repository.observeCart().first()?.items?.size)
    }
}