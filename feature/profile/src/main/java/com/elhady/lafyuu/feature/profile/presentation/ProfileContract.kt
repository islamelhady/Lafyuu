package com.elhady.lafyuu.feature.profile.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.auth.domain.model.User

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userProfile: User? = null,
    val error: UiText? = null
)

sealed interface ProfileUiEvent {
    data object LoadProfile : ProfileUiEvent
    data object ChangePasswordClicked : ProfileUiEvent
    data object LogoutClicked : ProfileUiEvent
    data object RetryClicked : ProfileUiEvent
    data object BackClicked: ProfileUiEvent
}

sealed interface ProfileUiEffect {
    data object NavigateToChangePassword : ProfileUiEffect
    data object NavigateToLogin : ProfileUiEffect
    data object NavigateBack: ProfileUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : ProfileUiEffect
}
