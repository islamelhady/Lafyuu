package com.elhady.lafyuu.feature.auth.presentation.otp

data class OtpUiState(
    val email: String = "",
    val otp: String = "",
    val isLoading: Boolean = false,
    val otpError: String? = null,
    val generalError: String? = null
)

sealed interface OtpUiEvent {
    data class OtpChanged(val otp: String) : OtpUiEvent
    data object VerifyClicked : OtpUiEvent
    data object ResendOtpClicked : OtpUiEvent
}

sealed interface OtpUiEffect {
    data object NavigateToLogin : OtpUiEffect
    data class ShowError(val message: String) : OtpUiEffect
    data class ShowMessage(val message: String) : OtpUiEffect
}
