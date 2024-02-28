package com.elhady.lafyuu.feature.auth.presentation.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<LoginUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, error = null) }
            }
            is LoginUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password, error = null) }
            }
            LoginUiEvent.LoginClicked -> login()
            LoginUiEvent.GoogleLoginClicked -> { /* Handle Google Login */ }
        }
    }

    private fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(error = "Please fill in all fields") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = authRepository.login(email, password)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(LoginUiEffect.NavigateToHome)
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Login failed"
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                    _uiEffect.send(LoginUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> { /* Handled by initial state update */ }
            }
        }
    }
}
