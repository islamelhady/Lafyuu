package com.elhady.lafyuu.feature.auth.presentation.forgot_password

import com.elhady.lafyuu.core.ui.text.UiText

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val emailError: UiText? = null,
    val error: UiText? = null
)

sealed interface ForgotPasswordUiEvent {
    data class EmailChanged(val email: String) : ForgotPasswordUiEvent
    data object SubmitClicked : ForgotPasswordUiEvent
}

sealed interface ForgotPasswordUiEffect {
    data class NavigateToOtp(val email: String) : ForgotPasswordUiEffect
    data class ShowError(val message: UiText) : ForgotPasswordUiEffect
}
