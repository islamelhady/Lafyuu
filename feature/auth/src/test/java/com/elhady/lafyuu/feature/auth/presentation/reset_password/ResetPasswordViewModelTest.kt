package com.elhady.lafyuu.feature.auth.presentation.reset_password

import androidx.lifecycle.SavedStateHandle
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.usecase.ResetPasswordUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateConfirmPassword
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
class ResetPasswordViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val resetPasswordUseCase: ResetPasswordUseCase = mockk()
    private val validatePassword = ValidatePassword()
    private val validateConfirmPassword = ValidateConfirmPassword(validatePassword)
    private val savedStateHandle = SavedStateHandle(mapOf("email" to "test@example.com", "otp" to "123456"))

    private lateinit var viewModel: ResetPasswordViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ResetPasswordViewModel(resetPasswordUseCase, validatePassword, validateConfirmPassword, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `reset password with valid inputs triggers success`() = runTest {
        coEvery { resetPasswordUseCase("test@example.com", "123456", "Password123!") } returns AppResult.Success(Unit)

        viewModel.onEvent(ResetPasswordUiEvent.NewPasswordChanged("Password123!"))
        viewModel.onEvent(ResetPasswordUiEvent.ConfirmPasswordChanged("Password123!"))
        viewModel.onEvent(ResetPasswordUiEvent.ResetClicked)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(null, state.generalError)
    }

    @Test
    fun `reset password with invalid password shows error`() = runTest {
        viewModel.onEvent(ResetPasswordUiEvent.NewPasswordChanged("weak"))
        viewModel.onEvent(ResetPasswordUiEvent.ConfirmPasswordChanged("weak"))
        viewModel.onEvent(ResetPasswordUiEvent.ResetClicked)

        val state = viewModel.uiState.value
        assertNotNull(state.newPasswordError)
    }
}
