package com.elhady.lafyuu.feature.checkout.presentation

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.checkout.domain.model.ShippingAddress
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutAddressGateway
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelTest {

    private val gateway: CheckoutAddressGateway = mockk()

    private fun address(id: String) = ShippingAddress(id, "Cairo", "City $id", "Street", "1", "01012345678")

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { gateway.getAddresses() } returns AppResult.Success(listOf(address("a-1"), address("a-2")))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = CheckoutViewModel(gateway)

    @Test
    fun `refresh loads the addresses and selects the first one`() = runTest {
        val viewModel = createViewModel()

        viewModel.onIntent(CheckoutUiIntent.Refresh)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(listOf("a-1", "a-2"), state.addresses.map { it.id })
        assertEquals("a-1", state.selectedAddressId)
        assertEquals("City a-1, Cairo", state.addresses.first().title)
    }

    @Test
    fun `failed load with no data is an error and not the empty state`() = runTest {
        coEvery { gateway.getAddresses() } returns AppResult.Failure(AppError.NoInternet)
        val viewModel = createViewModel()

        viewModel.onIntent(CheckoutUiIntent.Refresh)

        assertNotNull(viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isEmpty)
    }

    @Test
    fun `next navigates to payment with the selected address`() = runTest {
        val viewModel = createViewModel()
        viewModel.onIntent(CheckoutUiIntent.Refresh)
        val effects = mutableListOf<CheckoutUiEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.toList(effects)
        }

        viewModel.onIntent(CheckoutUiIntent.AddressSelected("a-2"))
        viewModel.onIntent(CheckoutUiIntent.NextClicked)

        assertEquals(listOf<CheckoutUiEffect>(CheckoutUiEffect.NavigateToPayment("a-2")), effects)
    }

    @Test
    fun `next without an address shows an error snackbar`() = runTest {
        coEvery { gateway.getAddresses() } returns AppResult.Success(emptyList())
        val viewModel = createViewModel()
        viewModel.onIntent(CheckoutUiIntent.Refresh)
        val effects = mutableListOf<CheckoutUiEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.toList(effects)
        }

        viewModel.onIntent(CheckoutUiIntent.NextClicked)

        assertEquals(AlertType.Error, (effects.single() as CheckoutUiEffect.ShowSnackbar).type)
    }

    @Test
    fun `delete needs confirmation then removes locally and moves the selection`() = runTest {
        coEvery { gateway.deleteAddress("a-1") } returns AppResult.Success(Unit)
        val viewModel = createViewModel()
        viewModel.onIntent(CheckoutUiIntent.Refresh)

        viewModel.onIntent(CheckoutUiIntent.DeleteAddressClicked("a-1"))
        coVerify(exactly = 0) { gateway.deleteAddress(any()) }
        viewModel.onIntent(CheckoutUiIntent.DeleteConfirmed)

        val state = viewModel.uiState.value
        assertEquals(listOf("a-2"), state.addresses.map { it.id })
        assertEquals("a-2", state.selectedAddressId)
        assertTrue(state.pendingDelete == null)
        coVerify(exactly = 1) { gateway.getAddresses() } // no reload after delete
    }
}