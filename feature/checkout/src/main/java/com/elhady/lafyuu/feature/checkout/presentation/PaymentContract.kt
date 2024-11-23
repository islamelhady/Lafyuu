package com.elhady.lafyuu.feature.checkout.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.checkout.domain.model.PaymentMethod

sealed interface PaymentStage {
    /** The user is choosing a method and has not placed an order yet */
    data object Selecting : PaymentStage

    /** The order exists, only the payment page is missing. Paying again would create a second order. */
    data class AwaitingPayment(val paymentUrl: String) : PaymentStage

    data class Completed(val message: UiText) : PaymentStage
}

data class PaymentUiState(
    val isLoading: Boolean = true,
    val loadError: UiText? = null,
    val total: Double? = null,
    val couponCode: String? = null,
    val selectedMethod: PaymentMethod = PaymentMethod.CREDIT_CARD,
    val isCheckingOut: Boolean = false,
    val stage: PaymentStage = PaymentStage.Selecting,
    // Must never be lost, so it is state (not an effect). The screen opens it then sends UrlOpened.
    val urlToOpen: String? = null
)

sealed interface PaymentUiIntent {
    data class MethodSelected(val method: PaymentMethod) : PaymentUiIntent
    data object PayClicked : PaymentUiIntent
    data object RetryClicked : PaymentUiIntent
    data object UrlOpened : PaymentUiIntent
    data object OpenPaymentPageClicked : PaymentUiIntent
    data object ViewOrdersClicked : PaymentUiIntent
    data object BackClicked : PaymentUiIntent
}

sealed interface PaymentUiEffect {
    data object NavigateBack : PaymentUiEffect
    data object NavigateToOrders : PaymentUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : PaymentUiEffect
}
