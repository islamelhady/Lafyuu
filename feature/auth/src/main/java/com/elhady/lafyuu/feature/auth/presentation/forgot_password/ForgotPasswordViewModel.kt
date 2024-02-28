package com.elhady.lafyuu.feature.auth.presentation.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.repository.AuthRepository
import com.elhady.lafyuu.feature.auth.presentation.forgot_password.ForgotPasswordUiEffect
import com.elhady.lafyuu.feature.auth.presentation.forgot_password.ForgotPasswordUiEvent
import com.elhady.lafyuu.feature.auth.presentation.forgot_password.ForgotPasswordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ForgotPasswordUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: ForgotPasswordUiEvent) {
        when (event) {
            is ForgotPasswordUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, error = null) }
            }
            ForgotPasswordUiEvent.SubmitClicked -> submit()
        }
    }

    private fun submit() {
        val email = _uiState.value.email
        if (email.isBlank()) {
            _uiState.update { it.copy(error = "Please enter your email") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.forgotPassword(email)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(ForgotPasswordUiEffect.NavigateToResetPassword(email))
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Request failed"
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                    _uiEffect.send(ForgotPasswordUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> {}
            }
        }
    }
}
