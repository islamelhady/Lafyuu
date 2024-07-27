package com.elhady.lafyuu.feature.checkout.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult

data class PaymentUiState(
    val isLoading: Boolean = false,
    val paymentMethods: List<String> = listOf("Credit Card", "Cash on Delivery", "Bank Transfer", "Paypal"),
    val selectedPaymentMethod: String = "Credit Card",
    val shippingAddressId: String = "",
    val checkoutTotal: Double = 0.0,
    val isCheckingOut: Boolean = false,
    val checkoutResult: CheckoutResult? = null,
    val isSuccess: Boolean = false,
    val error: String? = null
)

sealed interface PaymentUiEvent {
    data class SelectPaymentMethod(val method: String) : PaymentUiEvent
    data class PayClicked(val couponCode: String? = null) : PaymentUiEvent
    data object DismissSuccess : PaymentUiEvent
    data object BackClicked : PaymentUiEvent
}

sealed interface PaymentUiEffect {
    data object NavigateBack : PaymentUiEffect
    data class OpenExternalUrl(val url: String) : PaymentUiEffect
    data class ShowSnackBar(val message: String, val type: AlertType = AlertType.Success) : PaymentUiEffect
}
