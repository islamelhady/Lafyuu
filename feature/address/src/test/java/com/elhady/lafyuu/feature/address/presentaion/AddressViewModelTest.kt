package com.elhady.lafyuu.feature.address.presentation

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.usecase.DeleteAddressUseCase
import com.elhady.lafyuu.feature.address.domain.usecase.GetAddressesUseCase
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddressViewModelTest {

    private val getAddressesUseCase: GetAddressesUseCase = mockk()
    private val deleteAddressUseCase: DeleteAddressUseCase = mockk()

    private fun address(id: String) = Address(id, "Cairo", "City $id", "Street", "1", "01012345678", "")

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { getAddressesUseCase() } returns AppResult.Success(listOf(address("a-1"), address("a-2")))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = AddressViewModel(getAddressesUseCase, deleteAddressUseCase)

    @Test
    fun `nothing is loaded until the screen asks for a refresh`() = runTest {
        val viewModel = createViewModel()

        assertTrue(viewModel.uiState.value.isLoading)
        coVerify(exactly = 0) { getAddressesUseCase() }
    }

    @Test
    fun `refresh loads the addresses and selects the first one`() = runTest {
        val viewModel = createViewModel()

        viewModel.onIntent(AddressUiIntent.Refresh)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(listOf("a-1", "a-2"), state.addresses.map { it.id })
        assertEquals("a-1", state.selectedAddressId)
        assertEquals("City a-1, Cairo", state.addresses.first().title)
        assertEquals("+2 01012345678", state.addresses.first().phone)
    }

    @Test
    fun `failed load with no data shows an error and not the empty state`() = runTest {
        coEvery { getAddressesUseCase() } returns AppResult.Failure(AppError.NoInternet)
        val viewModel = createViewModel()

        viewModel.onIntent(AddressUiIntent.Refresh)

        val state = viewModel.uiState.value
        assertNotNull(state.error)
        assertFalse(state.isEmpty)
    }

    @Test
    fun `empty result is the empty state`() = runTest {
        coEvery { getAddressesUseCase() } returns AppResult.Success(emptyList())
        val viewModel = createViewModel()

        viewModel.onIntent(AddressUiIntent.Refresh)

        assertTrue(viewModel.uiState.value.isEmpty)
    }

    @Test
    fun `delete needs a confirmation`() = runTest {
        val viewModel = createViewModel()
        viewModel.onIntent(AddressUiIntent.Refresh)

        viewModel.onIntent(AddressUiIntent.DeleteAddressClicked("a-1"))

        assertEquals("a-1", viewModel.uiState.value.pendingDelete?.id)
        coVerify(exactly = 0) { deleteAddressUseCase(any()) }

        viewModel.onIntent(AddressUiIntent.DeleteDismissed)
        assertNull(viewModel.uiState.value.pendingDelete)
    }

    @Test
    fun `confirmed delete removes the item locally and moves the selection`() = runTest {
        coEvery { deleteAddressUseCase("a-1") } returns AppResult.Success(Unit)
        val viewModel = createViewModel()
        viewModel.onIntent(AddressUiIntent.Refresh)
        viewModel.onIntent(AddressUiIntent.DeleteAddressClicked("a-1"))

        viewModel.onIntent(AddressUiIntent.DeleteConfirmed)

        val state = viewModel.uiState.value
        assertEquals(listOf("a-2"), state.addresses.map { it.id })
        assertEquals("a-2", state.selectedAddressId)
        assertNull(state.pendingDelete)
        coVerify(exactly = 1) { getAddressesUseCase() } // no reload after delete
    }

    @Test
    fun `failed delete keeps the list and shows a snackbar`() = runTest {
        coEvery { deleteAddressUseCase("a-1") } returns AppResult.Failure(AppError.NoInternet)
        val viewModel = createViewModel()
        viewModel.onIntent(AddressUiIntent.Refresh)
        val effects = mutableListOf<AddressUiEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.toList(effects)
        }
        viewModel.onIntent(AddressUiIntent.DeleteAddressClicked("a-1"))

        viewModel.onIntent(AddressUiIntent.DeleteConfirmed)

        assertEquals(2, viewModel.uiState.value.addresses.size)
        assertEquals(AlertType.Error, (effects.single() as AddressUiEffect.ShowSnackbar).type)
    }
}