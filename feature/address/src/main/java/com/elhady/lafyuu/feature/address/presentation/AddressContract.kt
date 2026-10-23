package com.elhady.lafyuu.feature.address.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.address.domain.model.Address

data class AddressUiState(
    val isLoading: Boolean = false,
    val addresses: List<Address> = emptyList(),
    val selectedAddressId: String? = null,
    val error: UiText? = null
)

sealed interface AddressUiEvent {
    data object LoadAddresses : AddressUiEvent
    data class AddressSelected(val addressId: String) : AddressUiEvent
    data class EditAddressClicked(val addressId: String) : AddressUiEvent
    data class DeleteAddressClicked(val addressId: String) : AddressUiEvent
    data object AddAddressClicked : AddressUiEvent
    data object BackClicked : AddressUiEvent
}

sealed interface AddressUiEffect {
    data object NavigateBack : AddressUiEffect
    data object NavigateToAddAddress : AddressUiEffect
    data class NavigateToEditAddress(val addressId: String) : AddressUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : AddressUiEffect
}
