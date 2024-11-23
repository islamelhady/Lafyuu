package com.elhady.lafyuu.feature.checkout.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.checkout.presentation.model.ShippingAddressUi

data class CheckoutUiState(
    val isLoading: Boolean = true,
    val addresses: List<ShippingAddressUi> = emptyList(),
    val selectedAddressId: String? = null,
    val error: UiText? = null,
    val pendingDelete: ShippingAddressUi? = null,
    val isDeleting: Boolean = false
) {
    val isEmpty: Boolean get() = !isLoading && error == null && addresses.isEmpty()
}

sealed interface CheckoutUiIntent {
    data object Refresh : CheckoutUiIntent
    data object RetryClicked : CheckoutUiIntent
    data class AddressSelected(val addressId: String) : CheckoutUiIntent
    data object AddAddressClicked : CheckoutUiIntent
    data class EditAddressClicked(val addressId: String) : CheckoutUiIntent
    data class DeleteAddressClicked(val addressId: String) : CheckoutUiIntent
    data object DeleteConfirmed : CheckoutUiIntent
    data object DeleteDismissed : CheckoutUiIntent
    data object NextClicked : CheckoutUiIntent
    data object BackClicked : CheckoutUiIntent
}

sealed interface CheckoutUiEffect {
    data object NavigateBack : CheckoutUiEffect
    data class NavigateToPayment(val shippingAddressId: String) : CheckoutUiEffect
    data object NavigateToAddAddress : CheckoutUiEffect
    data class NavigateToEditAddress(val addressId: String) : CheckoutUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Success) : CheckoutUiEffect
}
