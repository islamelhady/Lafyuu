package com.elhady.lafyuu.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import com.elhady.lafyuu.feature.auth.domain.usecase.RegisterUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateConfirmPassword
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateEmail
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateName
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
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val validateName: ValidateName,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateConfirmPassword: ValidateConfirmPassword
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<RegisterUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.FirstNameChanged -> {
                _uiState.update { it.copy(firstName = event.name, firstNameError = null, generalError = null) }
            }
            is RegisterUiEvent.LastNameChanged -> {
                _uiState.update { it.copy(lastName = event.name, lastNameError = null, generalError = null) }
            }
            is RegisterUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, emailError = null, generalError = null) }
            }
            is RegisterUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password, passwordError = null, generalError = null) }
            }
            is RegisterUiEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.password, confirmPasswordError = null, generalError = null) }
            }
            RegisterUiEvent.RegisterClicked -> register()
        }
    }

    private fun register() {
        if (_uiState.value.isLoading) return

        val state = _uiState.value
        val firstNameResult = validateName(state.firstName)
        val lastNameResult = validateName(state.lastName)
        val emailResult = validateEmail(state.email)
        val passwordResult = validatePassword(state.password)
        val confirmPasswordResult = validateConfirmPassword(state.password, state.confirmPassword)

        val hasError = listOf(firstNameResult, lastNameResult, emailResult, passwordResult, confirmPasswordResult)
            .any { it is ValidationResult.Error }

        if (hasError) {
            _uiState.update {
                it.copy(
                    firstNameError = if (firstNameResult is ValidationResult.Error.NameRequired) "First name is required" else null,
                    lastNameError = if (lastNameResult is ValidationResult.Error.NameRequired) "Last name is required" else null,
                    emailError = when (emailResult) {
                        ValidationResult.Error.EmailRequired -> "Email is required"
                        ValidationResult.Error.InvalidEmailFormat -> "Invalid email format"
                        else -> null
                    },
                    passwordError = when (passwordResult) {
                        ValidationResult.Error.PasswordRequired -> "Password is required"
                        ValidationResult.Error.PasswordMissingDigit -> "Password must contain at least one digit"
                        ValidationResult.Error.PasswordMissingUppercase -> "Password must contain at least one uppercase letter"
                        ValidationResult.Error.PasswordMissingSpecialChar -> "Password must contain at least one special character"
                        else -> null
                    },
                    confirmPasswordError = when (confirmPasswordResult) {
                        ValidationResult.Error.ConfirmPasswordRequired -> "Confirm password is required"
                        ValidationResult.Error.PasswordMismatch -> "Passwords do not match"
                        else -> null
                    }
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = registerUseCase(state.email, state.password, state.firstName, state.lastName)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(RegisterUiEffect.NavigateToEmailVerification(state.email))
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Registration failed"
                    _uiState.update { it.copy(isLoading = false, generalError = errorMessage) }
                    _uiEffect.send(RegisterUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> {}
            }
        }
    }
}
