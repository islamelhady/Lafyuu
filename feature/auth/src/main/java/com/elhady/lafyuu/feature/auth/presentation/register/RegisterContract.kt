package com.elhady.lafyuu.feature.auth.presentation.register

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null
)

sealed interface RegisterUiEvent {
    data class FirstNameChanged(val name: String) : RegisterUiEvent
    data class LastNameChanged(val name: String) : RegisterUiEvent
    data class EmailChanged(val email: String) : RegisterUiEvent
    data class PasswordChanged(val password: String) : RegisterUiEvent
    data class ConfirmPasswordChanged(val password: String) : RegisterUiEvent
    data object RegisterClicked : RegisterUiEvent
}

sealed interface RegisterUiEffect {
    data object NavigateToLogin : RegisterUiEffect
    data object NavigateToOtp : RegisterUiEffect
    data class ShowError(val message: String) : RegisterUiEffect
}
