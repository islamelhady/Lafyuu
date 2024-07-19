package com.elhady.lafyuu.feature.cart.presentation

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.model.CartItem
import com.elhady.lafyuu.feature.cart.domain.usecase.ApplyCouponUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.DecreaseCartItemUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.GetCartUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.RemoveCartItemUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.UpdateCartItemQuantityUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getCartUseCase: GetCartUseCase = mockk()
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase = mockk()
    private val decreaseCartItemUseCase: DecreaseCartItemUseCase = mockk()
    private val removeCartItemUseCase: RemoveCartItemUseCase = mockk()
    private val applyCouponUseCase: ApplyCouponUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads cart successfully on init`() = runTest {
        val cart = Cart(
            cartId = "c-100",
            items = listOf(CartItem(itemId = "i-1", productId = "p-1", productName = "Running Shoes", productCoverUrl = "", productStock = 10, weightInGrams = 500.0, quantity = 1, discountPercentage = 0.0, basePricePerUnit = 100.0, finalPricePerUnit = 100.0, totalPrice = 100.0))
        )
        coEvery { getCartUseCase() } returns AppResult.Success(cart)

        val viewModel = CartViewModel(
            getCartUseCase,
            updateCartItemQuantityUseCase,
            decreaseCartItemUseCase,
            removeCartItemUseCase,
            applyCouponUseCase
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("c-100", state.cart?.cartId)
        assertEquals(1, state.items.size)
        assertEquals("Running Shoes", state.items[0].productName)
    }

    @Test
    fun `CheckoutClicked emits NavigateToCheckout effect`() = runTest {
        coEvery { getCartUseCase() } returns AppResult.Success(Cart(cartId = "c-1", items = emptyList()))
        val viewModel = CartViewModel(
            getCartUseCase,
            updateCartItemQuantityUseCase,
            decreaseCartItemUseCase,
            removeCartItemUseCase,
            applyCouponUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(CartUiEvent.CheckoutClicked)

        val effect = viewModel.uiEffect.first()
        assertTrue(effect is CartUiEffect.NavigateToCheckout)
    }
}
