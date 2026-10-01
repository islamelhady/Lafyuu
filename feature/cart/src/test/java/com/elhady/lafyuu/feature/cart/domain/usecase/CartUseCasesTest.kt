package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.model.CartItem
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
    fun `IncreaseCartItemQuantityUseCase prevents exceeding stock`() = runTest {
        val useCase = IncreaseCartItemQuantityUseCase(repository)
        val item = CartItem(
            itemId = "item-1", productId = "p-1", productName = "Shoe",
            productCoverUrl = "", productStock = 2, weightInGrams = 100.0,
            quantity = 2, discountPercentage = 0.0, basePricePerUnit = 50.0,
            finalPricePerUnit = 50.0, totalPrice = 100.0
        )
        val cart = Cart(cartId = "c-1", items = listOf(item))
        coEvery { repository.getCart() } returns AppResult.Success(cart)

        val result = useCase("item-1")

        assertTrue(result is AppResult.Error)
        assertEquals("Cannot exceed available stock (2)", (result as AppResult.Error).message)
    }

    @Test
    fun `ApplyCouponUseCase validates non-blank coupon code`() = runTest {
        val useCase = ApplyCouponUseCase(repository)

        val result = useCase("")

        assertTrue(result is AppResult.Error)
        assertEquals("Please enter a coupon code", (result as AppResult.Error).message)
    }
}
