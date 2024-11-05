package com.elhady.lafyuu.feature.auth.presentation.email_verification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.auth.R
import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import com.elhady.lafyuu.feature.auth.domain.usecase.ResendOtpUseCase
import com.elhady.lafyuu.feature.auth.domain.usecase.VerifyEmailUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateOtp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val resendOtpUseCase: ResendOtpUseCase,
    private val validateOtp: ValidateOtp,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val email: String = savedStateHandle.get<String>("email") ?: ""
    private var timerJob: Job? = null

    private val _uiState = MutableStateFlow(EmailVerificationUiState(email = email))
    val uiState: StateFlow<EmailVerificationUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<EmailVerificationUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        startResendTimer()
    }

    private fun startResendTimer(initialSeconds: Int = 45) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _uiState.update { it.copy(timerSeconds = initialSeconds, isResendEnabled = false) }
            for (i in (initialSeconds - 1) downTo 0) {
                delay(1000L)
                _uiState.update { it.copy(timerSeconds = i) }
            }
            _uiState.update { it.copy(isResendEnabled = true) }
        }
    }

    fun onEvent(event: EmailVerificationUiEvent) {
        when (event) {
            is EmailVerificationUiEvent.OtpChanged -> {
                _uiState.update { it.copy(otp = event.otp, otpError = null, error = null) }
            }
            EmailVerificationUiEvent.VerifyClicked -> verify()
            EmailVerificationUiEvent.ResendOtpClicked -> resendOtp()
        }
    }

    private fun verify() {
        if (_uiState.value.isLoading) return

        val otp = _uiState.value.otp
        val otpResult = validateOtp(otp)

        if (otpResult is ValidationResult.Error) {
            _uiState.update { it.copy(otpError = UiText.Resource(R.string.otp_is_required)) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = verifyEmailUseCase(email, otp)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(EmailVerificationUiEffect.NavigateToLogin)
                }
                is AppResult.Failure -> {
                    val errorMessage = result.error.toUiText()
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                    _uiEffect.send(EmailVerificationUiEffect.ShowSnackbar(errorMessage, type = AlertType.Error))
                }
            }
        }
    }

    private fun resendOtp() {
        if (_uiState.value.isLoading || !_uiState.value.isResendEnabled) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = resendOtpUseCase(email)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(EmailVerificationUiEffect.ShowSnackbar(message = UiText.Resource(R.string.otp_resent_successfully), type = AlertType.Success))
                    startResendTimer(45)
                }
                is AppResult.Failure -> {
                    val errorMessage = result.error.toUiText()
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                    _uiEffect.send(EmailVerificationUiEffect.ShowSnackbar(errorMessage, type = AlertType.Error))
                }
            }
        }
    }
}
