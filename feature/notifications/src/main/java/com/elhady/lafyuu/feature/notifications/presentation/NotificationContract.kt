package com.elhady.lafyuu.feature.notifications.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.notifications.presentation.model.NotificationUi

data class NotificationUiState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val hasNextPage: Boolean = false,
    val notifications: List<NotificationUi> = emptyList(),
    val error: UiText? = null
)

sealed interface NotificationUiIntent {
    data object RetryClicked : NotificationUiIntent
    data object LoadMore : NotificationUiIntent
    data class NotificationClicked(val notificationId: String) : NotificationUiIntent
    data object BackClicked : NotificationUiIntent
}

sealed interface NotificationUiEffect {
    data object NavigateBack : NotificationUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : NotificationUiEffect
}
