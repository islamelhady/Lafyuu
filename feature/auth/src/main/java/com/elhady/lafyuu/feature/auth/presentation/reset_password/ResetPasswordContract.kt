package com.elhady.lafyuu.feature.auth.presentation.reset_password

data class ResetPasswordUiState(
    val email: String = "",
    val otp: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val otpError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null
)

sealed interface ResetPasswordUiEvent {
    data class OtpChanged(val otp: String) : ResetPasswordUiEvent
    data class NewPasswordChanged(val password: String) : ResetPasswordUiEvent
    data class ConfirmPasswordChanged(val password: String) : ResetPasswordUiEvent
    data object ResetClicked : ResetPasswordUiEvent
}

sealed interface ResetPasswordUiEffect {
    data object NavigateToLogin : ResetPasswordUiEffect
    data class ShowError(val message: String) : ResetPasswordUiEffect
}
