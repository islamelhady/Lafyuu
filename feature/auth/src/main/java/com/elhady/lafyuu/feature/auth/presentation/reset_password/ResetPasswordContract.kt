package com.elhady.lafyuu.feature.auth.presentation.reset_password

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText

data class ResetPasswordUiState(
    val email: String = "",
    val otp: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val otpError: UiText? = null,
    val newPasswordError: UiText? = null,
    val confirmPasswordError: UiText? = null,
    val generalError: UiText? = null
)

sealed interface ResetPasswordUiEvent {
    data class OtpChanged(val otp: String) : ResetPasswordUiEvent
    data class NewPasswordChanged(val password: String) : ResetPasswordUiEvent
    data class ConfirmPasswordChanged(val password: String) : ResetPasswordUiEvent
    data object ResetClicked : ResetPasswordUiEvent
}

sealed interface ResetPasswordUiEffect {
    data object NavigateToLogin : ResetPasswordUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Success) : ResetPasswordUiEffect
}
