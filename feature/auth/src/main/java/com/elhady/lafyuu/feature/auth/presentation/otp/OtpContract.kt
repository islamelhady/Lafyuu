package com.elhady.lafyuu.feature.auth.presentation.otp

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText

data class OtpUiState(
    val email: String = "",
    val otp: String = "",
    val isLoading: Boolean = false,
    val otpError: UiText? = null,
    val generalError: UiText? = null
)

sealed interface OtpUiEvent {
    data class OtpChanged(val otp: String) : OtpUiEvent
    data object VerifyClicked : OtpUiEvent
    data object ResendOtpClicked : OtpUiEvent
}

sealed interface OtpUiEffect {
    data class NavigateToResetPassword(val email: String, val otp: String) : OtpUiEffect
    data object NavigateToLogin : OtpUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Success) : OtpUiEffect
}
