package com.elhady.lafyuu.feature.profile.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.profile.domain.model.UserProfile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
    val error: String? = null
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
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : ProfileUiEffect
}
