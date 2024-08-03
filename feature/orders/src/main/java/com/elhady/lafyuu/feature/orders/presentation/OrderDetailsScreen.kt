package com.elhady.lafyuu.feature.orders.presentation

import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.other.TrackingStepper
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistory
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistoryEntry

@Composable
fun OrderDetailsRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrderDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                OrderDetailsUiEffect.NavigateBack -> onNavigateBack()
                is OrderDetailsUiEffect.ShowSnackbar -> {
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

    OrderDetailsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun OrderDetailsScreen(
    uiState: OrderDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (OrderDetailsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Order Details",
                leadingIcon = Left,
                onLeadingClick = { onEvent(OrderDetailsUiEvent.BackClicked) }
            )
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(Theme.space.large)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Theme.color.blue
                    )
                }

                uiState.orderHistory != null -> {
                    val history = uiState.orderHistory!!
                    val steps = listOf("Packing", "Shipping", "Arriving", "Success")
                    val currentStatus = history.history.lastOrNull()?.status ?: "Packing"
                    val currentStep = when {
                        currentStatus.contains(
                            "success",
                            ignoreCase = true
                        ) || currentStatus.contains("delivered", ignoreCase = true) -> 3

                        currentStatus.contains("arriv", ignoreCase = true) -> 2
                        currentStatus.contains("ship", ignoreCase = true) -> 1
                        else -> 0
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(Theme.space.large)
                    ) {

                        TrackingStepper(
                            steps = steps,
                            currentStep = currentStep
                        )
                    }
                }

                else -> {
                    InfoStateContent(
                        errorMessage = "No order details found",
                        onRetryClick = { onEvent(OrderDetailsUiEvent.RetryClicked) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderDetailsScreenPreview() {
    val uiState = OrderDetailsUiState(
        isLoading = false,
        orderHistory = OrderHistory(
            orderId = "123456",
            orderCode = "ABC123",
            history = listOf(
                OrderHistoryEntry(
                    status = "Packing",
                    changeDate = "2023-08-01",
                    notes = "Preparing for shipping"
                ),
                OrderHistoryEntry(
                    status = "Shipping",
                    changeDate = "2023-08-02",
                    notes = "In transit"
                ),
                OrderHistoryEntry(
                    status = "Arriving",
                    changeDate = "2023-08-03",
                    notes = "Arrived at destination"
                )
            )
        ),
        error = null
    )
    val snackbarHostState = remember { SnackbarHostState() }
    OrderDetailsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = {}
    )
}
