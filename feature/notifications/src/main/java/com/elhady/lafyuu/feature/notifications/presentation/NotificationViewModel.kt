package com.elhady.lafyuu.feature.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.notifications.domain.usecase.GetNotificationsUseCase
import com.elhady.lafyuu.feature.notifications.domain.usecase.MarkNotificationAsReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<NotificationUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadNotifications()
    }

    fun onEvent(event: NotificationUiEvent) {
        when (event) {
            NotificationUiEvent.LoadNotifications -> loadNotifications()
            is NotificationUiEvent.NotificationClicked -> markAsRead(event.notificationId)
            NotificationUiEvent.BackClicked -> sendEffect(NotificationUiEffect.NavigateBack)
            NotificationUiEvent.RetryClicked -> loadNotifications()
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getNotificationsUseCase()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notifications = result.data,
                            error = null
                        )
                    }
                }

                is AppResult.Error -> {
                    val message = result.message ?: "Failed to load notifications"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = message
                        )
                    }
                    sendEffect(NotificationUiEffect.ShowSnackbar(message, type = AlertType.Error))
                }

                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            when (val result = markNotificationAsReadUseCase(notificationId)) {
                is AppResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            notifications = state.notifications.map { notification ->
                                if (notification.id == notificationId) notification.copy(isRead = true) else notification
                            }
                        )
                    }
                }

                is AppResult.Error -> {
                    val message = result.message ?: "Failed to mark as read"
                    sendEffect(NotificationUiEffect.ShowSnackbar(message, type = AlertType.Error))
                }

                is AppResult.Loading -> {}
            }
        }
    }

    private fun sendEffect(effect: NotificationUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
