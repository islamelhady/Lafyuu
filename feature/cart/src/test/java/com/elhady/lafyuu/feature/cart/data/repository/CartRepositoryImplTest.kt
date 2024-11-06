package com.elhady.lafyuu.feature.cart.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.AppResult
import com.elhady.lafyuu.feature.cart.data.remote.CartApi
import com.elhady.lafyuu.feature.cart.data.remote.model.ApplyCouponResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.GetCartResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CartRepositoryImplTest {

    private val cartApi: CartApi = mockk()
    private val appResult: AppResult = mockk(relaxed = true)

    private lateinit var repository: CartRepositoryImpl

    @Before
    fun setUp() {
        repository = CartRepositoryImpl(cartApi, appResult)
    }

    @Test
    fun `getCart returns success when response is successful`() = runTest {
        val dto = GetCartResponseDto(cartId = "c-1", cartItems = emptyList())
        coEvery { cartApi.getCart() } returns Response.success(dto)

        val result = repository.getCart()

        assertTrue(result is AppResult.Success)
        assertEquals("c-1", (result as AppResult.Success).data.cartId)
    }

    @Test(expected = CancellationException::class)
    fun `getCart rethrows CancellationException`() = runTest {
        coEvery { cartApi.getCart() } throws CancellationException("Coroutine cancelled")

        repository.getCart()
    }

    @Test(expected = CancellationException::class)
    fun `updateItemQuantity rethrows CancellationException from refreshCartUnlocked`() = runTest {
        coEvery { cartApi.updateCartItem(any(), any()) } returns Response.success(mockk(relaxed = true))
        coEvery { cartApi.getCart() } throws CancellationException("Coroutine cancelled")

        repository.updateItemQuantity("i-1", 2)
    }

    @Test
    fun `applyCoupon returns success when response is successful`() = runTest {
        val dto = ApplyCouponResponseDto(cartId = "c-1", finalTotal = 50.0)
        coEvery { cartApi.applyCoupon(any()) } returns Response.success(dto)

        val result = repository.applyCoupon("SUMMER")

        assertTrue(result is AppResult.Success)
        assertEquals(50.0, (result as AppResult.Success).data.finalTotal!!, 0.001)
    }

    @Test
    fun `removeItem returns success when response is successful`() = runTest {
        coEvery { cartApi.deleteCartItem(any()) } returns Response.success(Unit)
        coEvery { cartApi.getCart() } returns Response.success(GetCartResponseDto(cartId = "c-1", cartItems = emptyList()))

        val result = repository.removeItem("i-1")

        assertTrue(result is AppResult.Success)
    }
}
