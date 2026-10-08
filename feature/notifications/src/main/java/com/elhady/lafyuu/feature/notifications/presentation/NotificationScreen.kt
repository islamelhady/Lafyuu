package com.elhady.lafyuu.feature.notifications.presentation

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.list.NotificationListItem
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.icons.Transaction
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun NotificationRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                NotificationUiEffect.NavigateBack -> onNavigateBack()
                is NotificationUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        visuals = LafyuuSnackBarVisuals(
                            message = effect.message,
                            type = effect.type
                        )
                    )
                }
            }
        }
    }

    NotificationScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun NotificationScreen(
    uiState: NotificationUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (NotificationUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Notification",
                leadingIcon = Left,
                onLeadingClick = { onEvent(NotificationUiEvent.BackClicked) }
            )
        }
    ) {
        when {
            uiState.isLoading && uiState.notifications.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )
            }

            uiState.notifications.isEmpty() -> {
                InfoStateContent(
                    errorMessage = "No notifications found",
                    onRetryClick = { onEvent(NotificationUiEvent.RetryClicked) },
                    errorType = AlertType.Error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.small)
                ) {
                    items(
                        items = uiState.notifications,
                        key = { it.id }) { item ->
                        val backgroundColor = if (!item.isRead) {
                            Theme.color.blue.copy(alpha = 0.1f)
                        } else {
                            Theme.color.backgroundWhite
                        }

                        NotificationListItem(
                            title = if (item.isRead) "Notification" else "New Notification",
                            description = item.notificationText,
                            date = item.createdAt ?: "",
                            onClick = {
                                onEvent(
                                    NotificationUiEvent.NotificationClicked(
                                        notificationId = item.id
                                    )
                                )
                            },
                            leadingIcon = Transaction,
                            backgroundColor = backgroundColor
                        )
                    }
                }
            }
        }
    }
}
