package com.elhady.lafyuu.feature.auth.presentation.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import com.elhady.lafyuu.feature.auth.domain.usecase.ResendOtpUseCase
import com.elhady.lafyuu.feature.auth.domain.usecase.VerifyEmailUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateOtp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val resendOtpUseCase: ResendOtpUseCase,
    private val validateOtp: ValidateOtp,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val email: String = checkNotNull(savedStateHandle["email"])

    private val _uiState = MutableStateFlow(OtpUiState(email = email))
    val uiState: StateFlow<OtpUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<OtpUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: OtpUiEvent) {
        when (event) {
            is OtpUiEvent.OtpChanged -> {
                _uiState.update { it.copy(otp = event.otp, otpError = null, generalError = null) }
            }
            OtpUiEvent.VerifyClicked -> verify()
            OtpUiEvent.ResendOtpClicked -> resendOtp()
        }
    }

    private fun verify() {
        if (_uiState.value.isLoading) return

        val otp = _uiState.value.otp
        val otpResult = validateOtp(otp)

        if (otpResult is ValidationResult.Error) {
            _uiState.update { it.copy(otpError = "OTP is required") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = verifyEmailUseCase(email, otp)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(OtpUiEffect.NavigateToLogin)
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Verification failed"
                    _uiState.update { it.copy(isLoading = false, generalError = errorMessage) }
                    _uiEffect.send(OtpUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> {}
            }
        }
    }

    private fun resendOtp() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = resendOtpUseCase(email)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(OtpUiEffect.ShowMessage("OTP resent successfully"))
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Resend failed"
                    _uiState.update { it.copy(isLoading = false, generalError = errorMessage) }
                    _uiEffect.send(OtpUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> {}
            }
        }
    }
}
