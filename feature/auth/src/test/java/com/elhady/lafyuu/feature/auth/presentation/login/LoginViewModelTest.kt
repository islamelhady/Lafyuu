package com.elhady.lafyuu.feature.auth.presentation.login

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.auth.domain.model.AuthToken
import com.elhady.lafyuu.feature.auth.domain.usecase.GoogleLoginUseCase
import com.elhady.lafyuu.feature.auth.domain.usecase.LoginUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateEmail
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateLoginPassword
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
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val loginUseCase: LoginUseCase = mockk()
    private val googleLoginUseCase: GoogleLoginUseCase = mockk()
    private val validateEmail = ValidateEmail()
    private val validateLoginPassword = ValidateLoginPassword()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase, googleLoginUseCase, validateEmail, validateLoginPassword)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with valid inputs triggers success`() = runTest {
        val token = AuthToken("access", "refresh", "2026-12-31")
        coEvery { loginUseCase("test@example.com", "Password123!") } returns AppResult.Success(token)

        viewModel.onEvent(LoginUiEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("Password123!"))
        viewModel.onEvent(LoginUiEvent.LoginClicked)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(null, state.generalError)
    }

    @Test
    fun `login with invalid email shows email error`() = runTest {
        viewModel.onEvent(LoginUiEvent.EmailChanged("invalid-email"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("Password123!"))
        viewModel.onEvent(LoginUiEvent.LoginClicked)

        val state = viewModel.uiState.value
        assertNotNull(state.emailError)
    }
}
