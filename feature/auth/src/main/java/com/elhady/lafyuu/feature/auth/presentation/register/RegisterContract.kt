package com.elhady.lafyuu.feature.auth.presentation.register

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val firstNameError: UiText? = null,
    val lastNameError: UiText? = null,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val confirmPasswordError: UiText? = null,
    val generalError: UiText? = null
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
    data class NavigateToEmailVerification(val email: String) : RegisterUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Success) : RegisterUiEffect
}
