package com.elhady.lafyuu.feature.profile.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType

data class ChangePasswordUiState(
    val isLoading: Boolean = false,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val error: String? = null
)

sealed interface ChangePasswordUiEvent {
    data class CurrentPasswordChanged(val value: String) : ChangePasswordUiEvent
    data class NewPasswordChanged(val value: String) : ChangePasswordUiEvent
    data class ConfirmNewPasswordChanged(val value: String) : ChangePasswordUiEvent
    data object SubmitClicked : ChangePasswordUiEvent
    data object BackClicked : ChangePasswordUiEvent
}

sealed interface ChangePasswordUiEffect {
    data object NavigateBack : ChangePasswordUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Success) : ChangePasswordUiEffect
}
