package com.elhady.lafyuu.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import com.elhady.lafyuu.feature.auth.domain.usecase.GoogleLoginUseCase
import com.elhady.lafyuu.feature.auth.domain.usecase.LoginUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateEmail
import com.elhady.lafyuu.feature.auth.domain.validator.ValidatePassword
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
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<LoginUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, emailError = null, generalError = null) }
            }
            is LoginUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password, passwordError = null, generalError = null) }
            }
            LoginUiEvent.LoginClicked -> login()
            is LoginUiEvent.GoogleLoginClicked -> googleLogin(event.idToken)
        }
    }

    private fun login() {
        if (_uiState.value.isLoading) return

        val state = _uiState.value
        val emailResult = validateEmail(state.email)
        val passwordResult = validatePassword(state.password)

        val hasError = emailResult is ValidationResult.Error || passwordResult is ValidationResult.Error

        if (hasError) {
            _uiState.update {
                it.copy(
                    emailError = when (emailResult) {
                        is ValidationResult.Error -> "Oops! Your Email Is Not Correct"
                        else -> null
                    },
                    passwordError = when (passwordResult) {
                        is ValidationResult.Error -> "Oops! Your Password Is Not Correct"
                        else -> null
                    }
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = loginUseCase(state.email, state.password)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(LoginUiEffect.NavigateToHome)
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Login failed"
                    _uiState.update { it.copy(isLoading = false, generalError = errorMessage) }
                    _uiEffect.send(LoginUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> {}
            }
        }
    }

    private fun googleLogin(idToken: String) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = googleLoginUseCase(idToken)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(LoginUiEffect.NavigateToHome)
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Google login failed"
                    _uiState.update { it.copy(isLoading = false, generalError = errorMessage) }
                    _uiEffect.send(LoginUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> {}
            }
        }
    }
}
