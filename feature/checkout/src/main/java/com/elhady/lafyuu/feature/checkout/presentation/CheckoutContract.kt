package com.elhady.lafyuu.feature.checkout.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.checkout.domain.model.Address

enum class AddressField {
    STATE, CITY, STREET, APARTMENT, PHONE, NOTES
}

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val addresses: List<Address> = emptyList(),
    val selectedAddress: Address? = null,
    val selectedPaymentMethod: String = "Credit Card",
    val paymentMethods: List<String> = listOf("Credit Card", "Cash on Delivery", "Bank Transfer"),
    val error: String? = null
)

sealed interface CheckoutUiEvent {
    data object LoadAddresses : CheckoutUiEvent
    data class SelectAddress(val address: Address) : CheckoutUiEvent
    data class SelectPaymentMethod(val method: String) : CheckoutUiEvent
    data object OpenAddAddress : CheckoutUiEvent
    data class OpenEditAddress(val addressId: String) : CheckoutUiEvent
    data class DeleteAddress(val addressId: String) : CheckoutUiEvent
    data object NextClicked : CheckoutUiEvent
    data object RetryClicked : CheckoutUiEvent
    data object BackClicked : CheckoutUiEvent
}

sealed interface CheckoutUiEffect {
    data object NavigateBack : CheckoutUiEffect
    data class NavigateToPayment(val shippingAddressId: String, val paymentMethod: String) : CheckoutUiEffect
    data object NavigateToAddAddress : CheckoutUiEffect
    data class NavigateToEditAddress(val addressId: String) : CheckoutUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Success) : CheckoutUiEffect
}
