package com.elhady.lafyuu.feature.orders.presentation

import Left
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.other.TrackingStepper
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Bank
import com.elhady.lafyuu.core.designsystem.icons.CreditCard
import com.elhady.lafyuu.core.designsystem.icons.Paypal
import com.elhady.lafyuu.core.designsystem.icons.Transaction
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
                        Column(verticalArrangement = Arrangement.spacedBy(Theme.space.extraSmall)) {
                            LafyuuText(
                                text = "Order Code: ${history.orderCode}",
                                style = Theme.typography.heading5,
                                color = Theme.color.neutralDark
                            )
                            if (!history.createdAt.isNullOrBlank()) {
                                LafyuuText(
                                    text = "Ordered on: ${history.createdAt}",
                                    style = Theme.typography.normalCaptionRegular,
                                    color = Theme.color.neutralGrey
                                )
                            }
                        }

                        HorizontalDivider(color = Theme.color.neutralLight)

                        LafyuuText(
                            text = "Order Status",
                            style = Theme.typography.heading6,
                            color = Theme.color.neutralDark
                        )

                        TrackingStepper(
                            steps = steps,
                            currentStep = currentStep
                        )

                        if (!history.paymentMethod.isBlank()) {
                            SectionTitle("Price Details")
                            OrderPaymentDetailsSection(
                                paymentMethod = history.paymentMethod,
                                totalPrice = history.totalPrice
                            )
                        }

                        HorizontalDivider(color = Theme.color.neutralLight)

                        LafyuuText(
                            text = "Tracking History",
                            style = Theme.typography.heading5,
                            color = Theme.color.neutralDark
                        )

                        if (history.history.isEmpty()) {
                            LafyuuText(
                                text = "No tracking history available.",
                                style = Theme.typography.normalTextRegular,
                                color = Theme.color.neutralGrey
                            )
                        } else {
                            history.history.forEach { entry ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Theme.color.backgroundWhite,
                                            shape = Theme.corner.small
                                        )
                                        .padding(Theme.space.medium),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        LafyuuText(
                                            text = entry.status,
                                            style = Theme.typography.mediumTextBold,
                                            color = Theme.color.blue
                                        )
                                        if (!entry.changeDate.isNullOrBlank()) {
                                            LafyuuText(
                                                text = entry.changeDate,
                                                style = Theme.typography.normalCaptionRegular,
                                                color = Theme.color.neutralGrey
                                            )
                                        }
                                    }
                                    if (!entry.notes.isNullOrBlank()) {
                                        LafyuuText(
                                            text = entry.notes,
                                            style = Theme.typography.normalTextRegular,
                                            color = Theme.color.neutralDark
                                        )
                                    }
                                }
                            }
                        }
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

@SuppressLint("DefaultLocale")
@Composable
fun OrderPaymentDetailsSection(
    paymentMethod: String,
    totalPrice: Double
) {
    val mappedMethod = when {
        paymentMethod.contains("card", ignoreCase = true) -> "Credit Card Or Debit"
        paymentMethod.contains("paypal", ignoreCase = true) -> "Paypal"
        paymentMethod.contains("bank", ignoreCase = true) -> "Bank Transfer"
        else -> paymentMethod
    }
    val icon = when (mappedMethod) {
        "Credit Card Or Debit" -> CreditCard
        "Paypal" -> Paypal
        "Bank Transfer" -> Bank
        else -> Transaction
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = Theme.corner.small,
        colors = CardDefaults.cardColors(containerColor = Theme.color.backgroundWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(Theme.space.large)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
            ) {
                LafyuuText(
                    text = "Payment Method",
                    style = Theme.typography.normalTextRegular,
                    color = Theme.color.neutralDark,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Theme.color.blue,
                    modifier = Modifier.size(24.dp)
                )
                LafyuuText(
                    text = mappedMethod,
                    style = Theme.typography.normalTextRegular,
                    color = Theme.color.neutralGrey
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LafyuuText(
                    text = "Total Price",
                    style = Theme.typography.normalTextRegular,
                    color = Theme.color.neutralGrey
                )
                LafyuuText(
                    text = "$${String.format("%.2f", totalPrice)}",
                    style = Theme.typography.heading5,
                    color = Theme.color.blue
                )
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
            totalPrice = 150.75,
            paymentMethod = "Credit Card",
            createdAt = "2023-08-01",
            updatedAt = "2023-08-02",
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
