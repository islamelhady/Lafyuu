package com.elhady.lafyuu.feature.notifications.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.notifications.domain.model.Notification

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: UiText? = null
)

sealed interface NotificationUiEvent {
    data object LoadNotifications : NotificationUiEvent
    data class NotificationClicked(val notificationId: String) : NotificationUiEvent
    data object BackClicked : NotificationUiEvent
    data object RetryClicked : NotificationUiEvent
}

sealed interface NotificationUiEffect {
    data object NavigateBack : NotificationUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : NotificationUiEffect
}
