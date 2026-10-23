package com.elhady.lafyuu.feature.auth.presentation.email_verification

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText

data class EmailVerificationUiState(
    val email: String = "",
    val otp: String = "",
    val isLoading: Boolean = false,
    val timerSeconds: Int = 45,
    val isResendEnabled: Boolean = false,
    val otpError: UiText? = null,
    val error: UiText? = null
)

sealed interface EmailVerificationUiEvent {
    data class OtpChanged(val otp: String) : EmailVerificationUiEvent
    data object VerifyClicked : EmailVerificationUiEvent
    data object ResendOtpClicked : EmailVerificationUiEvent
}

sealed interface EmailVerificationUiEffect {
    data object NavigateToLogin : EmailVerificationUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Success) : EmailVerificationUiEffect
}
