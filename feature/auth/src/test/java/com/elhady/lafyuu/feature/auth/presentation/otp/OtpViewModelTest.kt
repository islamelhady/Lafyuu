package com.elhady.lafyuu.feature.auth.presentation.otp

import androidx.lifecycle.SavedStateHandle
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.usecase.ResendOtpUseCase
import com.elhady.lafyuu.feature.auth.domain.usecase.ValidateOtpUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateOtp
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
class OtpViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val validateOtpUseCase: ValidateOtpUseCase = mockk()
    private val resendOtpUseCase: ResendOtpUseCase = mockk()
    private val validateOtp = ValidateOtp()
    private val savedStateHandle = SavedStateHandle(mapOf("email" to "test@example.com"))

    private lateinit var viewModel: OtpViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = OtpViewModel(validateOtpUseCase, resendOtpUseCase, validateOtp, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify with valid otp triggers success`() = runTest {
        coEvery { validateOtpUseCase("test@example.com", "123456") } returns AppResult.Success(Unit)

        viewModel.onEvent(OtpUiEvent.OtpChanged("123456"))
        viewModel.onEvent(OtpUiEvent.VerifyClicked)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(null, state.generalError)
    }

    @Test
    fun `verify with empty otp shows error`() = runTest {
        viewModel.onEvent(OtpUiEvent.OtpChanged(""))
        viewModel.onEvent(OtpUiEvent.VerifyClicked)

        val state = viewModel.uiState.value
        assertNotNull(state.otpError)
    }
}
