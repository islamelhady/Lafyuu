package com.elhady.lafyuu.feature.orders.presentation

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
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
import com.elhady.lafyuu.core.designsystem.components.card.OrderCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun OrdersRoute(
    onNavigateBack: () -> Unit,
    onNavigateToOrderDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                OrdersUiEffect.NavigateBack -> onNavigateBack()
                is OrdersUiEffect.NavigateToOrderDetails -> onNavigateToOrderDetails(effect.orderId)
                is OrdersUiEffect.ShowSnackbar -> {
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

    OrdersScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun OrdersScreen(
    uiState: OrdersUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (OrdersUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Orders",
                leadingIcon = Left,
                onLeadingClick = { onEvent(OrdersUiEvent.BackClicked) }
            )
        }
    ) {
        when {
            uiState.isLoading && uiState.orders.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )
            }

            uiState.orders.isEmpty() -> {
                InfoStateContent(
                    errorMessage = "No orders found",
                    errorType = AlertType.Warning,
                    onRetryClick = { onEvent(OrdersUiEvent.RetryClicked) }
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                ) {
                    items(uiState.orders) { order ->
                        OrderCard(
                            orderCode = order.orderCode,
                            updatedAt = order.updatedAt,
                            status = order.status,
                            totalPrice = order.totalPrice,
                            paymentMethod = order.paymentMethod,
                            onClick = { onEvent(OrdersUiEvent.SelectOrder(order.orderId)) }
                        )
                    }
                }
            }
        }
    }
}