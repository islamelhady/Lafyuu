package com.elhady.lafyuu.feature.auth.presentation.register

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.usecase.RegisterUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateConfirmPassword
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateEmail
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateName
import com.elhady.lafyuu.feature.auth.domain.validator.ValidatePassword
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val registerUseCase: RegisterUseCase = mockk()
    private val validateName = ValidateName()
    private val validateEmail = ValidateEmail()
    private val validatePassword = ValidatePassword()
    private val validateConfirmPassword = ValidateConfirmPassword(validatePassword)

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewModel(registerUseCase, validateName, validateEmail, validatePassword, validateConfirmPassword)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `register with valid inputs triggers success`() = runTest {
        coEvery { registerUseCase("test@example.com", "Password123!", "John", "Doe") } returns AppResult.Success(Unit)

        viewModel.onEvent(RegisterUiEvent.FirstNameChanged("John"))
        viewModel.onEvent(RegisterUiEvent.LastNameChanged("Doe"))
        viewModel.onEvent(RegisterUiEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(RegisterUiEvent.PasswordChanged("Password123!"))
        viewModel.onEvent(RegisterUiEvent.ConfirmPasswordChanged("Password123!"))
        viewModel.onEvent(RegisterUiEvent.RegisterClicked)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(null, state.generalError)
    }

    @Test
    fun `register with empty first name shows error`() = runTest {
        viewModel.onEvent(RegisterUiEvent.FirstNameChanged(""))
        viewModel.onEvent(RegisterUiEvent.RegisterClicked)

        val state = viewModel.uiState.value
        assertNotNull(state.firstNameError)
    }
}
