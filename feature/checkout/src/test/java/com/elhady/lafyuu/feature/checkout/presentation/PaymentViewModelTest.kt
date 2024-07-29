package com.elhady.lafyuu.feature.checkout.presentation

import androidx.lifecycle.SavedStateHandle
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.usecase.GetCartUseCase
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.usecase.CheckoutUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val checkoutUseCase: CheckoutUseCase = mockk()
    private val getCartUseCase: GetCartUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `PayClicked triggers checkout and sets success state`() = runTest {
        coEvery { getCartUseCase() } returns AppResult.Success(Cart(cartId = "c-1", items = emptyList(), finalTotal = 100.0))
        coEvery { checkoutUseCase("a-1", "Credit Card Or Debit", null) } returns AppResult.Success(CheckoutResult("Paid successfully", null, null))

        val savedStateHandle = SavedStateHandle(mapOf("shippingAddressId" to "a-1"))
        val viewModel = PaymentViewModel(checkoutUseCase, getCartUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(PaymentUiEvent.PayClicked(null))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isSuccess)
        assertEquals("Paid successfully", state.checkoutResult?.message)
        assertEquals(100.0, state.checkoutTotal, 0.001)
    }
}
