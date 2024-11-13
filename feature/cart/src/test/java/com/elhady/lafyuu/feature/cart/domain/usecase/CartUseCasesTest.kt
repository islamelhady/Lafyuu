package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.model.CartItem
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CartUseCasesTest {

    private val repository: CartRepository = mockk()

    private fun item(quantity: Int = 1, stock: Int = 10) = CartItem(
        itemId = "item-1", productId = "p-1", productName = "Shoe",
        productCoverUrl = "", productStock = stock, weightInGrams = 100.0,
        quantity = quantity, discountPercentage = 0.0, basePricePerUnit = 50.0,
        finalPricePerUnit = 50.0, totalPrice = 50.0 * quantity
    )

    private fun cachedCart(vararg items: CartItem) {
        every { repository.observeCart() } returns flowOf(Cart(cartId = "c-1", items = items.toList()))
    }

    @Test
    fun `GetCartUseCase invokes repository getCart`() = runTest {
        val cart = Cart(cartId = "1", items = emptyList())
        coEvery { repository.getCart() } returns AppResult.Success(cart)

        assertEquals(AppResult.Success(cart), GetCartUseCase(repository)())
    }

    @Test
    fun `increase prevents exceeding stock`() = runTest {
        cachedCart(item(quantity = 2, stock = 2))

        val result = IncreaseCartItemQuantityUseCase(repository)("item-1")

        assertEquals(AppResult.Failure(AppError.OutOfStock(2)), result)
    }

    @Test
    fun `increase sends quantity plus one`() = runTest {
        cachedCart(item(quantity = 2, stock = 5))
        coEvery { repository.updateItemQuantity("item-1", 3) } returns AppResult.Success(Unit)

        val result = IncreaseCartItemQuantityUseCase(repository)("item-1")

        assertEquals(AppResult.Success(Unit), result)
        coVerify(exactly = 1) { repository.updateItemQuantity("item-1", 3) }
    }

    @Test
    fun `increase returns NotFound for an unknown item`() = runTest {
        cachedCart()

        val result = IncreaseCartItemQuantityUseCase(repository)("item-1")

        assertEquals(AppResult.Failure(AppError.NotFound), result)
    }

    @Test
    fun `decrease removes the item when quantity is one`() = runTest {
        cachedCart(item(quantity = 1))
        coEvery { repository.removeItem("item-1") } returns AppResult.Success(Unit)

        DecreaseCartItemUseCase(repository)("item-1")

        coVerify(exactly = 1) { repository.removeItem("item-1") }
    }

    @Test
    fun `decrease decrements when quantity is more than one`() = runTest {
        cachedCart(item(quantity = 3))
        coEvery { repository.decrementItem("item-1", 1) } returns AppResult.Success(Unit)

        DecreaseCartItemUseCase(repository)("item-1")

        coVerify(exactly = 1) { repository.decrementItem("item-1", 1) }
    }

    @Test
    fun `ApplyCouponUseCase rejects a blank code`() = runTest {
        val result = ApplyCouponUseCase(repository)("   ")

        assertEquals(AppResult.Failure(AppError.Validation("Coupon code is empty")), result)
        coVerify(exactly = 0) { repository.applyCoupon(any()) }
    }

    @Test
    fun `ApplyCouponUseCase trims the code`() = runTest {
        val cart = Cart(cartId = "c-1", items = emptyList())
        coEvery { repository.applyCoupon("SALE10") } returns AppResult.Success(cart)

        val result = ApplyCouponUseCase(repository)("  SALE10 ")

        assertEquals(AppResult.Success(cart), result)
    }
}