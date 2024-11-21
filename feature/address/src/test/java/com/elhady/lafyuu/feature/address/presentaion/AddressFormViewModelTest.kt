package com.elhady.lafyuu.feature.address.presentation

import androidx.lifecycle.SavedStateHandle
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.model.AddressField
import com.elhady.lafyuu.feature.address.domain.model.AddressInput
import com.elhady.lafyuu.feature.address.domain.usecase.CreateAddressUseCase
import com.elhady.lafyuu.feature.address.domain.usecase.GetAddressUseCase
import com.elhady.lafyuu.feature.address.domain.usecase.UpdateAddressUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
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
class AddressFormViewModelTest {

    private val getAddressUseCase: GetAddressUseCase = mockk()
    private val createAddressUseCase: CreateAddressUseCase = mockk()
    private val updateAddressUseCase: UpdateAddressUseCase = mockk()

    private val address = Address("a-1", "Cairo", "Nasr City", "Street 9", "12", "01012345678", "gate 2")

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(addressId: String? = null) = AddressFormViewModel(
        getAddressUseCase,
        createAddressUseCase,
        updateAddressUseCase,
        SavedStateHandle(
            if (addressId == null) emptyMap()
            else mapOf(AddressFormViewModel.ADDRESS_ID_ARG to addressId)
        )
    )

    private fun AddressFormViewModel.fillValidForm() {
        onIntent(AddressFormUiIntent.FieldChanged(AddressField.STATE, " Cairo "))
        onIntent(AddressFormUiIntent.FieldChanged(AddressField.CITY, "Nasr City"))
        onIntent(AddressFormUiIntent.FieldChanged(AddressField.STREET, "Street 9"))
        onIntent(AddressFormUiIntent.FieldChanged(AddressField.APARTMENT, "12"))
        onIntent(AddressFormUiIntent.FieldChanged(AddressField.PHONE, "01012345678"))
    }

    @Test
    fun `add mode does not fetch anything`() = runTest {
        val state = createViewModel().uiState.value

        assertFalse(state.isEditMode)
        assertFalse(state.isFetching)
        coVerify(exactly = 0) { getAddressUseCase(any()) }
    }

    @Test
    fun `saving an empty form shows errors on required fields only`() = runTest {
        val viewModel = createViewModel()

        viewModel.onIntent(AddressFormUiIntent.SaveClicked)

        assertEquals(
            setOf(AddressField.STATE, AddressField.CITY, AddressField.STREET, AddressField.APARTMENT, AddressField.PHONE),
            viewModel.uiState.value.errors.keys
        )
        coVerify(exactly = 0) { createAddressUseCase(any()) }
    }

    @Test
    fun `editing a field clears only its error`() = runTest {
        val viewModel = createViewModel()
        viewModel.onIntent(AddressFormUiIntent.SaveClicked)

        viewModel.onIntent(AddressFormUiIntent.FieldChanged(AddressField.CITY, "Giza"))

        val errors = viewModel.uiState.value.errors
        assertFalse(AddressField.CITY in errors)
        assertTrue(AddressField.STATE in errors)
    }

    @Test
    fun `valid save creates a trimmed address and navigates back`() = runTest {
        val expected = AddressInput("Cairo", "Nasr City", "Street 9", "12", "01012345678", "")
        coEvery { createAddressUseCase(any()) } returns AppResult.Success(address)
        val viewModel = createViewModel()
        val effects = mutableListOf<AddressFormUiEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.toList(effects)
        }
        viewModel.fillValidForm()

        viewModel.onIntent(AddressFormUiIntent.SaveClicked)

        coVerify(exactly = 1) { createAddressUseCase(expected) }
        assertEquals(listOf<AddressFormUiEffect>(AddressFormUiEffect.NavigateBack), effects)
        assertFalse(viewModel.uiState.value.isSaving)
    }

    @Test
    fun `a second save while saving is ignored`() = runTest {
        val gate = CompletableDeferred<Unit>()
        coEvery { createAddressUseCase(any()) } coAnswers {
            gate.await()
            AppResult.Success(address)
        }
        val viewModel = createViewModel()
        viewModel.fillValidForm()

        viewModel.onIntent(AddressFormUiIntent.SaveClicked)
        viewModel.onIntent(AddressFormUiIntent.SaveClicked)
        gate.complete(Unit)

        coVerify(exactly = 1) { createAddressUseCase(any()) }
    }

    @Test
    fun `server field errors are shown under the matching field`() = runTest {
        coEvery { createAddressUseCase(any()) } returns AppResult.Failure(
            AppError.Validation("bad", mapOf("PhoneNumber" to listOf("Phone is taken")))
        )
        val viewModel = createViewModel()
        viewModel.fillValidForm()

        viewModel.onIntent(AddressFormUiIntent.SaveClicked)

        assertEquals(UiText.Dynamic("Phone is taken"), viewModel.uiState.value.errors[AddressField.PHONE])
        assertFalse(viewModel.uiState.value.isSaving)
    }

    @Test
    fun `other failures show a snackbar`() = runTest {
        coEvery { createAddressUseCase(any()) } returns AppResult.Failure(AppError.NoInternet)
        val viewModel = createViewModel()
        val effects = mutableListOf<AddressFormUiEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.toList(effects)
        }
        viewModel.fillValidForm()

        viewModel.onIntent(AddressFormUiIntent.SaveClicked)

        val effect = effects.single() as AddressFormUiEffect.ShowSnackbar
        assertEquals(AlertType.Error, effect.type)
    }

    @Test
    fun `edit mode loads the address into the form and updates it`() = runTest {
        coEvery { getAddressUseCase("a-1") } returns AppResult.Success(address)
        coEvery { updateAddressUseCase("a-1", any()) } returns AppResult.Success(address)
        val viewModel = createViewModel("a-1")

        assertTrue(viewModel.uiState.value.isEditMode)
        assertEquals("Nasr City", viewModel.uiState.value.form.city)

        viewModel.onIntent(AddressFormUiIntent.SaveClicked)

        coVerify(exactly = 1) {
            updateAddressUseCase("a-1", AddressInput("Cairo", "Nasr City", "Street 9", "12", "01012345678", "gate 2"))
        }
    }

    @Test
    fun `edit mode load failure blocks saving until retry works`() = runTest {
        coEvery { getAddressUseCase("a-1") } returns AppResult.Failure(AppError.NoInternet)
        val viewModel = createViewModel("a-1")
        assertNotNull(viewModel.uiState.value.loadError)

        viewModel.onIntent(AddressFormUiIntent.SaveClicked)
        coVerify(exactly = 0) { updateAddressUseCase(any(), any()) }

        coEvery { getAddressUseCase("a-1") } returns AppResult.Success(address)
        viewModel.onIntent(AddressFormUiIntent.RetryLoadClicked)

        assertEquals(null, viewModel.uiState.value.loadError)
        assertEquals("Cairo", viewModel.uiState.value.form.state)
    }
}