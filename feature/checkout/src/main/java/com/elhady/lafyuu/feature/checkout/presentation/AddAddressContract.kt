package com.elhady.lafyuu.feature.checkout.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType

data class AddAddressUiState(
    val isLoading: Boolean = false,
    val state: String = "",
    val city: String = "",
    val street: String = "",
    val apartment: String = "",
    val phoneNumber: String = "",
    val notes: String = "",
    val stateError: String? = null,
    val cityError: String? = null,
    val streetError: String? = null,
    val apartmentError: String? = null,
    val phoneError: String? = null,
    val error: String? = null
)

sealed interface AddAddressUiEvent {
    data class FieldChanged(val field: AddressField, val value: String) : AddAddressUiEvent
    data object AddAddressClicked : AddAddressUiEvent
    data object BackClicked : AddAddressUiEvent
}

sealed interface AddAddressUiEffect {
    data object NavigateBack : AddAddressUiEffect
    data class ShowSnackBar(val message: String, val type: AlertType = AlertType.Error) : AddAddressUiEffect
}
