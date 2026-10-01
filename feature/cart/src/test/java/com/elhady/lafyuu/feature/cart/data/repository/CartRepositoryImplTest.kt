package com.elhady.lafyuu.feature.cart.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.feature.cart.data.remote.CartApi
import com.elhady.lafyuu.feature.cart.data.remote.model.ApplyCouponResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.GetCartResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CartRepositoryImplTest {

    private val cartApi: CartApi = mockk()
    private val apiErrorParser: ApiErrorParser = mockk(relaxed = true)

    private lateinit var repository: CartRepositoryImpl

    @Before
    fun setUp() {
        repository = CartRepositoryImpl(cartApi, apiErrorParser)
    }

    @Test
    fun `getCart returns success when response is successful`() = runTest {
        val dto = GetCartResponseDto(cartId = "c-1", cartItems = emptyList())
        coEvery { cartApi.getCart() } returns Response.success(dto)

        val result = repository.getCart()

        assertTrue(result is AppResult.Success)
        assertEquals("c-1", (result as AppResult.Success).data.cartId)
    }

    @Test
    fun `applyCoupon returns success when response is successful`() = runTest {
        val dto = ApplyCouponResponseDto(cartId = "c-1", finalTotal = 50.0)
        coEvery { cartApi.applyCoupon(any()) } returns Response.success(dto)

        val result = repository.applyCoupon("SUMMER")

        assertTrue(result is AppResult.Success)
        assertEquals(50.0, (result as AppResult.Success).data.finalTotal!!, 0.001)
    }
}
