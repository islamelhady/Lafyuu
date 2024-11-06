package com.elhady.lafyuu.feature.auth.presentation.reset_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.auth.R
import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import com.elhady.lafyuu.feature.auth.domain.usecase.ResetPasswordUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateConfirmPassword
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
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val validatePassword: ValidatePassword,
    private val validateConfirmPassword: ValidateConfirmPassword,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val email: String = checkNotNull(savedStateHandle["email"])
    private val otp: String = checkNotNull(savedStateHandle["otp"])

    private val _uiState = MutableStateFlow(ResetPasswordUiState(email = email, otp = otp))
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ResetPasswordUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: ResetPasswordUiEvent) {
        when (event) {
            is ResetPasswordUiEvent.OtpChanged -> {
                _uiState.update { it.copy(otp = event.otp, otpError = null, generalError = null) }
            }
            is ResetPasswordUiEvent.NewPasswordChanged -> {
                _uiState.update { it.copy(newPassword = event.password, newPasswordError = null, generalError = null) }
            }
            is ResetPasswordUiEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.password, confirmPasswordError = null, generalError = null) }
            }
            ResetPasswordUiEvent.ResetClicked -> reset()
        }
    }

    private fun reset() {
        if (_uiState.value.isLoading) return

        val state = _uiState.value
        val passwordResult = validatePassword(state.newPassword)
        val confirmPasswordResult = validateConfirmPassword(state.newPassword, state.confirmPassword)

        val hasError = listOf(passwordResult, confirmPasswordResult).any { it is ValidationResult.Error }

        if (hasError) {
            _uiState.update {
                it.copy(
                    newPasswordError = when (passwordResult) {
                        ValidationResult.Error.PasswordRequired -> UiText.Resource(R.string.new_password_is_required)
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
            when (val result = resetPasswordUseCase(email, otp, state.newPassword)) {
                is AppResult.Success<*> -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(ResetPasswordUiEffect.NavigateToLogin)
                }
                is AppResult.Failure -> {
                    val message = result.error.toUiText()
                    _uiState.update { it.copy(isLoading = false, generalError = message) }
                    _uiEffect.send(ResetPasswordUiEffect.ShowSnackbar(message))
                }
            }
        }
    }
}
