package com.elhady.lafyuu.feature.auth.presentation.email_verification

data class EmailVerificationUiState(
    val email: String = "",
    val otp: String = "",
    val isLoading: Boolean = false,
    val timerSeconds: Int = 45,
    val isResendEnabled: Boolean = false,
    val otpError: String? = null,
    val generalError: String? = null
)

sealed interface EmailVerificationUiEvent {
    data class OtpChanged(val otp: String) : EmailVerificationUiEvent
    data object VerifyClicked : EmailVerificationUiEvent
    data object ResendOtpClicked : EmailVerificationUiEvent
}

sealed interface EmailVerificationUiEffect {
    data object NavigateToLogin : EmailVerificationUiEffect
    data class ShowError(val message: String) : EmailVerificationUiEffect
    data class ShowSuccessMessage(val message: String) : EmailVerificationUiEffect
}
