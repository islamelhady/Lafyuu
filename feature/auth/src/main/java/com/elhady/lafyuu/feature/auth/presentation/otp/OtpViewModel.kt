package com.elhady.lafyuu.feature.auth.presentation.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.repository.AuthRepository
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
    private val authRepository: AuthRepository,
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
                _uiState.update { it.copy(otp = event.otp, error = null) }
            }
            OtpUiEvent.VerifyClicked -> verify()
            OtpUiEvent.ResendOtpClicked -> resendOtp()
        }
    }

    private fun verify() {
        val otp = _uiState.value.otp
        if (otp.length < 4) { // Assuming 4 digits based on standard OtpField
            _uiState.update { it.copy(error = "Please enter a valid OTP") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.verifyEmail(email, otp)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(OtpUiEffect.NavigateToLogin)
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Verification failed"
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                    _uiEffect.send(OtpUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> {}
            }
        }
    }

    private fun resendOtp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.resendOtp(email)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(OtpUiEffect.ShowMessage("OTP resent successfully"))
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Resend failed"
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                    _uiEffect.send(OtpUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> {}
            }
        }
    }
}
