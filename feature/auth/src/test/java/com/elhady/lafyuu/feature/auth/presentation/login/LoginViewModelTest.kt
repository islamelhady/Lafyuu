package com.elhady.lafyuu.feature.auth.presentation.login

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.model.AuthToken
import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import com.elhady.lafyuu.feature.auth.domain.usecase.GoogleLoginUseCase
import com.elhady.lafyuu.feature.auth.domain.usecase.LoginUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateEmail
import com.elhady.lafyuu.feature.auth.domain.validator.ValidatePassword
import io.mockk.coEvery
import io.mockk.every
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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var googleLoginUseCase: GoogleLoginUseCase
    private lateinit var validateEmail: ValidateEmail
    private lateinit var validatePassword: ValidatePassword
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginUseCase = mockk()
        googleLoginUseCase = mockk()
        validateEmail = mockk()
        validatePassword = mockk()

        every { validateEmail(any()) } returns ValidationResult.Success
        every { validatePassword(any()) } returns ValidationResult.Success

        viewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            googleLoginUseCase = googleLoginUseCase,
            validateEmail = validateEmail,
            validatePassword = validatePassword
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success emits NavigateToHome effect`() = runTest {
        val authToken = AuthToken("access", "refresh", "2026-12-31")
        coEvery { loginUseCase(any(), any()) } returns AppResult.Success(authToken)

        viewModel.onEvent(LoginUiEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("password123"))
        viewModel.onEvent(LoginUiEvent.LoginClicked)

        testDispatcher.scheduler.advanceUntilIdle()

        val effect = viewModel.uiEffect.first()
        assertEquals(LoginUiEffect.NavigateToHome, effect)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.generalError)
    }

    @Test
    fun `google login success emits NavigateToHome effect`() = runTest {
        val authToken = AuthToken("access", "refresh", "2026-12-31")
        coEvery { googleLoginUseCase("valid_id_token") } returns AppResult.Success(authToken)

        viewModel.onEvent(LoginUiEvent.GoogleLoginClicked("valid_id_token"))

        testDispatcher.scheduler.advanceUntilIdle()

        val effect = viewModel.uiEffect.first()
        assertEquals(LoginUiEffect.NavigateToHome, effect)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.generalError)
    }

    @Test
    fun `google login failure updates error state and emits ShowError effect`() = runTest {
        val errorMessage = "Invalid token"
        coEvery { googleLoginUseCase("invalid_token") } returns AppResult.Error(message = errorMessage)

        viewModel.onEvent(LoginUiEvent.GoogleLoginClicked("invalid_token"))

        testDispatcher.scheduler.advanceUntilIdle()

        val effect = viewModel.uiEffect.first()
        assertEquals(LoginUiEffect.ShowError(errorMessage), effect)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(errorMessage, viewModel.uiState.value.generalError)
    }
}
