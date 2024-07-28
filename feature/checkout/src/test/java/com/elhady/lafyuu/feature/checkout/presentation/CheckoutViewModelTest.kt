package com.elhady.lafyuu.feature.checkout.presentation

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.Address
import com.elhady.lafyuu.feature.checkout.domain.usecase.DeleteAddressUseCase
import com.elhady.lafyuu.feature.checkout.domain.usecase.GetAddressesUseCase
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
class CheckoutViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getAddressesUseCase: GetAddressesUseCase = mockk()
    private val deleteAddressUseCase: DeleteAddressUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads addresses successfully on init`() = runTest {
        val addressList = listOf(Address("a-1", "Cairo", "Cairo", "Street", "1", "123", ""))
        coEvery { getAddressesUseCase() } returns AppResult.Success(addressList)

        val viewModel = CheckoutViewModel(getAddressesUseCase, deleteAddressUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.addresses.size)
        assertEquals("a-1", state.selectedAddress?.id)
    }

    @Test
    fun `NextClicked emits NavigateToPayment effect when address selected`() = runTest {
        val addressList = listOf(Address("a-1", "Cairo", "Cairo", "Street", "1", "123", ""))
        coEvery { getAddressesUseCase() } returns AppResult.Success(addressList)

        val viewModel = CheckoutViewModel(getAddressesUseCase, deleteAddressUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(CheckoutUiEvent.NextClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        val effect = viewModel.uiEffect.first()
        assertTrue(effect is CheckoutUiEffect.NavigateToPayment)
        assertEquals("a-1", (effect as CheckoutUiEffect.NavigateToPayment).shippingAddressId)
    }
}
