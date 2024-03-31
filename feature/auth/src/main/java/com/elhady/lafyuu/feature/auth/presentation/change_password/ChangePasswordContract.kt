package com.elhady.lafyuu.feature.auth.presentation.change_password

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null
)

sealed interface ChangePasswordUiEvent {
    data class CurrentPasswordChanged(val password: String) : ChangePasswordUiEvent
    data class NewPasswordChanged(val password: String) : ChangePasswordUiEvent
    data class ConfirmPasswordChanged(val password: String) : ChangePasswordUiEvent
    data object SaveClicked : ChangePasswordUiEvent
}

sealed interface ChangePasswordUiEffect {
    data object NavigateBack : ChangePasswordUiEffect
    data class ShowError(val message: String) : ChangePasswordUiEffect
    data class ShowSuccessMessage(val message: String) : ChangePasswordUiEffect
}
