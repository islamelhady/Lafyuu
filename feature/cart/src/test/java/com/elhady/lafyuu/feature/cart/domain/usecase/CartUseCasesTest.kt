package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CartUseCasesTest {

    private val repository: CartRepository = mockk()

    @Test
    fun `GetCartUseCase invokes repository getCart`() = runTest {
        val useCase = GetCartUseCase(repository)
        val cart = Cart(cartId = "1", items = emptyList())
        coEvery { repository.getCart() } returns AppResult.Success(cart)

        val result = useCase()

        assertTrue(result is AppResult.Success)
        assertEquals("1", (result as AppResult.Success).data.cartId)
    }

    @Test
    fun `UpdateCartItemQuantityUseCase validates quantity greater than zero`() = runTest {
        val useCase = UpdateCartItemQuantityUseCase(repository)

        val result = useCase("item-1", 0)

        assertTrue(result is AppResult.Error)
        assertEquals("Quantity must be greater than zero", (result as AppResult.Error).message)
    }

    @Test
    fun `ApplyCouponUseCase validates non-blank coupon code`() = runTest {
        val useCase = ApplyCouponUseCase(repository)

        val result = useCase("")

        assertTrue(result is AppResult.Error)
        assertEquals("Please enter a coupon code", (result as AppResult.Error).message)
    }
}
