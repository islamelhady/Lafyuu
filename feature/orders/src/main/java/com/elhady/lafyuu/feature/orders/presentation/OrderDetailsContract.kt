package com.elhady.lafyuu.feature.orders.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistory

data class OrderDetailsUiState(
    val isLoading: Boolean = false,
    val orderId: String = "",
    val orderHistory: OrderHistory? = null,
    val error: String? = null
)

sealed interface OrderDetailsUiEvent {
    data class LoadOrderDetails(val orderId: String) : OrderDetailsUiEvent
    data object BackClicked : OrderDetailsUiEvent
    data object RetryClicked : OrderDetailsUiEvent
}

sealed interface OrderDetailsUiEffect {
    data object NavigateBack : OrderDetailsUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : OrderDetailsUiEffect
}
