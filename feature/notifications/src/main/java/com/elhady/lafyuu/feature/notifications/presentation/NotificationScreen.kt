package com.elhady.lafyuu.feature.notifications.presentation

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.list.NotificationListItem
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.icons.Transaction
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.notifications.R
import com.elhady.lafyuu.feature.notifications.presentation.model.NotificationUi
import kotlinx.coroutines.launch

private const val LOAD_MORE_THRESHOLD = 4

@Composable
fun NotificationRoute(
    onNavigateBack: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    NotificationUiEffect.NavigateBack -> onNavigateBack()

                    is NotificationUiEffect.ShowSnackbar -> {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        // separate coroutine: showSnackbar suspends until dismissed
                        launch {
                            snackbarHostState.showSnackbar(
                                visuals = LafyuuSnackBarVisuals(
                                    message = effect.message.asString(context),
                                    type = effect.type
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    NotificationScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun NotificationScreen(
    uiState: NotificationUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (NotificationUiIntent) -> Unit
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = stringResource(R.string.notifications_title),
                leadingIcon = Left,
                onLeadingClick = { onIntent(NotificationUiIntent.BackClicked) }
            )
        }
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Theme.color.blue
            )

            uiState.error != null && uiState.notifications.isEmpty() -> InfoStateContent(
                errorMessage = uiState.error.asString(),
                onRetryClick = { onIntent(NotificationUiIntent.RetryClicked) },
                errorType = AlertType.Error,
                modifier = Modifier.align(Alignment.Center)
            )

            uiState.notifications.isEmpty() -> InfoStateContent(
                errorMessage = stringResource(R.string.notifications_empty),
                errorType = AlertType.Warning,
                modifier = Modifier.align(Alignment.Center)
            )

            else -> NotificationList(
                notifications = uiState.notifications,
                isLoadingMore = uiState.isLoadingMore,
                onIntent = onIntent
            )
        }
    }
}

@Composable
private fun NotificationList(
    notifications: List<NotificationUi>,
    isLoadingMore: Boolean,
    onIntent: (NotificationUiIntent) -> Unit
) {
    val listState = rememberLazyListState()
    val isNearEnd by remember(listState) {
        derivedStateOf {
            val info = listState.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            info.totalItemsCount > 0 && lastVisible >= info.totalItemsCount - LOAD_MORE_THRESHOLD
        }
    }
    // keyed on size: after a page is appended the condition is evaluated again
    LaunchedEffect(isNearEnd, notifications.size) {
        if (isNearEnd) onIntent(NotificationUiIntent.LoadMore)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Theme.space.small)
    ) {
        items(items = notifications, key = { it.id }) { item ->
            NotificationListItem(
                title = stringResource(
                    if (item.isRead) R.string.notification_title_read
                    else R.string.notification_title_unread
                ),
                description = item.text,
                date = item.date,
                onClick = { onIntent(NotificationUiIntent.NotificationClicked(item.id)) },
                leadingIcon = Transaction,
                backgroundColor = if (item.isRead) {
                    Theme.color.backgroundWhite
                } else {
                    Theme.color.blue.copy(alpha = 0.1f)
                }
            )
        }
        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Theme.space.medium),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Theme.color.blue)
                }
            }
        }
    }
}