package com.elhady.lafyuu.feature.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.notifications.domain.usecase.GetNotificationsUseCase
import com.elhady.lafyuu.feature.notifications.domain.usecase.MarkNotificationAsReadUseCase
import com.elhady.lafyuu.feature.notifications.presentation.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiEffect = MutableSharedFlow<NotificationUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<NotificationUiEffect> = _uiEffect.asSharedFlow()

    private var loadJob: Job? = null
    private var loadMoreJob: Job? = null
    private var currentPage = GetNotificationsUseCase.FIRST_PAGE

    // ids with a mark-as-read request in flight (only touched on the main thread)
    private val pendingReadIds = mutableSetOf<String>()

    init {
        loadFirstPage()
    }

    fun onIntent(intent: NotificationUiIntent) {
        when (intent) {
            NotificationUiIntent.RetryClicked -> loadFirstPage()
            NotificationUiIntent.LoadMore -> loadMore()
            is NotificationUiIntent.NotificationClicked -> markAsRead(intent.notificationId)
            NotificationUiIntent.BackClicked -> sendEffect(NotificationUiEffect.NavigateBack)
        }
    }

    private fun loadFirstPage() {
        loadJob?.cancel()
        loadMoreJob?.cancel()
        currentPage = GetNotificationsUseCase.FIRST_PAGE
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isLoadingMore = false, error = null) }
            when (val result = getNotificationsUseCase(currentPage)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        notifications = result.data.items.map { item -> item.toUi() },
                        hasNextPage = result.data.hasNextPage
                    )
                }

                is AppResult.Failure -> _uiState.update {
                    it.copy(isLoading = false, error = result.error.toUiText())
                }
            }
        }
    }

    private fun loadMore() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.hasNextPage) return

        loadMoreJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            when (val result = getNotificationsUseCase(currentPage + 1)) {
                is AppResult.Success -> {
                    currentPage += 1
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            // distinctBy: a shifted page must never produce duplicate list keys
                            notifications = (it.notifications + result.data.items.map { item -> item.toUi() })
                                .distinctBy { item -> item.id },
                            hasNextPage = result.data.hasNextPage
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update { it.copy(isLoadingMore = false) }
                    _uiEffect.emit(
                        NotificationUiEffect.ShowSnackbar(result.error.toUiText(), AlertType.Error)
                    )
                }
            }
        }
    }

    private fun markAsRead(notificationId: String) {
        val notification = _uiState.value.notifications.firstOrNull { it.id == notificationId }
            ?: return
        if (notification.isRead || !pendingReadIds.add(notificationId)) return

        viewModelScope.launch {
            when (val result = markNotificationAsReadUseCase(notificationId)) {
                is AppResult.Success -> _uiState.update { state ->
                    state.copy(
                        notifications = state.notifications.map {
                            if (it.id == notificationId) it.copy(isRead = true) else it
                        }
                    )
                }

                is AppResult.Failure -> _uiEffect.emit(
                    NotificationUiEffect.ShowSnackbar(result.error.toUiText(), AlertType.Error)
                )
            }
            pendingReadIds.remove(notificationId)
        }
    }

    private fun sendEffect(effect: NotificationUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }
}