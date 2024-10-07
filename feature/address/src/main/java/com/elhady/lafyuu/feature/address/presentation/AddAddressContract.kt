package com.elhady.lafyuu.feature.address.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType

enum class AddressField {
    STATE, CITY, STREET, APARTMENT, PHONE, NOTES
}

data class AddAddressUiState(
    val state: String = "",
    val city: String = "",
    val street: String = "",
    val apartment: String = "",
    val phoneNumber: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
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
