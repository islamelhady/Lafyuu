package com.elhady.lafyuu.feature.auth.presentation.reset_password

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
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val email: String = checkNotNull(savedStateHandle["email"])

    private val _uiState = MutableStateFlow(ResetPasswordUiState(email = email))
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ResetPasswordUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: ResetPasswordUiEvent) {
        when (event) {
            is ResetPasswordUiEvent.OtpChanged -> {
                _uiState.update { it.copy(otp = event.otp, error = null) }
            }
            is ResetPasswordUiEvent.NewPasswordChanged -> {
                _uiState.update { it.copy(newPassword = event.password, error = null) }
            }
            is ResetPasswordUiEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.password, error = null) }
            }
            ResetPasswordUiEvent.ResetClicked -> reset()
        }
    }

    private fun reset() {
        val state = _uiState.value
        if (state.otp.isBlank() || state.newPassword.isBlank()) {
            _uiState.update { it.copy(error = "Please fill in all fields") }
            return
        }
        if (state.newPassword != state.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.resetPassword(email, state.otp, state.newPassword)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(ResetPasswordUiEffect.NavigateToLogin)
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Reset failed"
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                    _uiEffect.send(ResetPasswordUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> {}
            }
        }
    }
}
