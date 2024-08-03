package com.elhady.lafyuu.feature.orders.presentation

import androidx.lifecycle.SavedStateHandle
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistory
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistoryEntry
import com.elhady.lafyuu.feature.orders.domain.usecase.GetOrderHistoryUseCase
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
class OrderDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getOrderHistoryUseCase: GetOrderHistoryUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads order details successfully on init`() = runTest {
        val orderId = "order-123"
        val orderHistory = OrderHistory(
            orderId = orderId,
            orderCode = "LQNSU346JK",
            totalPrice = 250.50,
            paymentMethod = "Credit Card",
            createdAt = "2023-08-01",
            updatedAt = "2023-08-02",
            history = listOf(
                OrderHistoryEntry(status = "Packing", changeDate = "2023-08-01", notes = "Prepared")
            )
        )
        coEvery { getOrderHistoryUseCase(orderId) } returns AppResult.Success(orderHistory)

        val savedStateHandle = SavedStateHandle(mapOf("orderId" to orderId))
        val viewModel = OrderDetailsViewModel(getOrderHistoryUseCase, savedStateHandle)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(orderHistory, state.orderHistory)
        assertEquals("LQNSU346JK", state.orderHistory?.orderCode)
        assertEquals(250.50, state.orderHistory?.totalPrice ?: 0.0, 0.01)
        assertEquals("Credit Card", state.orderHistory?.paymentMethod)
    }

    @Test
    fun `loads order details failure emits error snackbar effect`() = runTest {
        val orderId = "order-123"
        coEvery { getOrderHistoryUseCase(orderId) } returns AppResult.Error("Network error")

        val savedStateHandle = SavedStateHandle(mapOf("orderId" to orderId))
        val viewModel = OrderDetailsViewModel(getOrderHistoryUseCase, savedStateHandle)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)

        val effect = viewModel.uiEffect.first()
        assertTrue(effect is OrderDetailsUiEffect.ShowSnackbar)
        assertEquals("Network error", (effect as OrderDetailsUiEffect.ShowSnackbar).message)
        assertEquals(AlertType.Error, effect.type)
    }
}
