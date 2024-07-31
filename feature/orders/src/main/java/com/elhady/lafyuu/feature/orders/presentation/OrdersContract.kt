package com.elhady.lafyuu.feature.orders.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.orders.domain.model.OrderSummary

data class OrdersUiState(
    val isLoading: Boolean = false,
    val orders: List<OrderSummary> = emptyList(),
    val error: String? = null
)

sealed interface OrdersUiEvent {
    data object LoadOrders : OrdersUiEvent
    data class SelectOrder(val orderId: String) : OrdersUiEvent
    data object BackClicked : OrdersUiEvent
    data object RetryClicked : OrdersUiEvent
}

sealed interface OrdersUiEffect {
    data object NavigateBack : OrdersUiEffect
    data class NavigateToOrderDetails(val orderId: String) : OrdersUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : OrdersUiEffect
}
