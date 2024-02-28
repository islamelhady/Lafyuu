package com.elhady.lafyuu.feature.auth.presentation.forgot_password

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface ForgotPasswordUiEvent {
    data class EmailChanged(val email: String) : ForgotPasswordUiEvent
    data object SubmitClicked : ForgotPasswordUiEvent
}

sealed interface ForgotPasswordUiEffect {
    data class NavigateToResetPassword(val email: String) : ForgotPasswordUiEffect
    data class ShowError(val message: String) : ForgotPasswordUiEffect
}
