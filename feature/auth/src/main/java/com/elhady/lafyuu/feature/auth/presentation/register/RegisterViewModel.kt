package com.elhady.lafyuu.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.auth.R
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
        val trimmedFirstName = state.firstName.trim()
        val trimmedLastName = state.lastName.trim()
        val trimmedEmail = state.email.trim()

        val firstNameResult = validateName(trimmedFirstName)
        val lastNameResult = validateName(trimmedLastName)
        val emailResult = validateEmail(trimmedEmail)
        val passwordResult = validatePassword(state.password)
        val confirmPasswordResult = validateConfirmPassword(state.password, state.confirmPassword)

        val hasError = listOf(firstNameResult, lastNameResult, emailResult, passwordResult, confirmPasswordResult)
            .any { it is ValidationResult.Error }

        if (hasError) {
            _uiState.update {
                it.copy(
                    firstNameError = if (firstNameResult is ValidationResult.Error.NameRequired) UiText.Resource(R.string.first_name_is_required) else null,
                    lastNameError = if (lastNameResult is ValidationResult.Error.NameRequired) UiText.Resource(R.string.last_name_is_required) else null,
                    emailError = when (emailResult) {
                        ValidationResult.Error.EmailRequired -> UiText.Resource(R.string.email_is_required)
                        ValidationResult.Error.InvalidEmailFormat -> UiText.Resource(R.string.invalid_email_format)
                        else -> null
                    },
                    passwordError = when (passwordResult) {
                        ValidationResult.Error.PasswordRequired -> UiText.Resource(R.string.password_is_required)
                        ValidationResult.Error.PasswordMissingDigit -> UiText.Resource(R.string.password_must_contain_at_least_one_digit)
                        ValidationResult.Error.PasswordMissingUppercase -> UiText.Resource(R.string.password_must_contain_at_least_one_uppercase_letter)
                        ValidationResult.Error.PasswordMissingSpecialChar -> UiText.Resource(R.string.password_must_contain_at_least_one_special_character)
                        else -> null
                    },
                    confirmPasswordError = when (confirmPasswordResult) {
                        ValidationResult.Error.ConfirmPasswordRequired -> UiText.Resource(R.string.confirm_password_is_required)
                        ValidationResult.Error.PasswordMismatch -> UiText.Resource(R.string.passwords_do_not_match)
                        else -> null
                    }
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = registerUseCase(trimmedEmail, state.password, trimmedFirstName, trimmedLastName)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(RegisterUiEffect.NavigateToEmailVerification(trimmedEmail))
                }
                is AppResult.Failure -> {
                    val message = result.error.toUiText()
                    _uiState.update { it.copy(isLoading = false, generalError = message) }
                    _uiEffect.send(RegisterUiEffect.ShowSnackbar(message))
                }
            }
        }
    }
}
