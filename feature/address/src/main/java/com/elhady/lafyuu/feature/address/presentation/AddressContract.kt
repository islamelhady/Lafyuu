package com.elhady.lafyuu.feature.address.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.address.presentation.model.AddressUi

data class AddressUiState(
    val isLoading: Boolean = true,
    val addresses: List<AddressUi> = emptyList(),
    val selectedAddressId: String? = null,
    val error: UiText? = null,
    val pendingDelete: AddressUi? = null,
    val isDeleting: Boolean = false
) {
    val isEmpty: Boolean get() = !isLoading && error == null && addresses.isEmpty()
}

sealed interface AddressUiIntent {
    data object Refresh : AddressUiIntent
    data object RetryClicked : AddressUiIntent
    data class AddressSelected(val addressId: String) : AddressUiIntent
    data class EditAddressClicked(val addressId: String) : AddressUiIntent
    data class DeleteAddressClicked(val addressId: String) : AddressUiIntent
    data object DeleteConfirmed : AddressUiIntent
    data object DeleteDismissed : AddressUiIntent
    data object AddAddressClicked : AddressUiIntent
    data object BackClicked : AddressUiIntent
}

sealed interface AddressUiEffect {
    data object NavigateBack : AddressUiEffect
    data object NavigateToAddAddress : AddressUiEffect
    data class NavigateToEditAddress(val addressId: String) : AddressUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : AddressUiEffect
}
