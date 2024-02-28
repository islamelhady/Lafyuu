package com.elhady.lafyuu.feature.auth.presentation.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object LoginClicked : LoginUiEvent
    data object GoogleLoginClicked : LoginUiEvent
}

sealed interface LoginUiEffect {
    data object NavigateToHome : LoginUiEffect
    data object NavigateToRegister : LoginUiEffect
    data object NavigateToForgotPassword : LoginUiEffect
    data class ShowError(val message: String) : LoginUiEffect
}
