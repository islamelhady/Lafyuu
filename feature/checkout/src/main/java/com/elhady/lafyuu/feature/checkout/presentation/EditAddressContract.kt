package com.elhady.lafyuu.feature.checkout.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType

data class EditAddressUiState(
    val isLoading: Boolean = false,
    val isFetching: Boolean = false,
    val addressId: String = "",
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

sealed interface EditAddressUiEvent {
    data class LoadAddress(val addressId: String) : EditAddressUiEvent
    data class FieldChanged(val field: AddressField, val value: String) : EditAddressUiEvent
    data object UpdateAddressClicked : EditAddressUiEvent
    data object BackClicked : EditAddressUiEvent
}

sealed interface EditAddressUiEffect {
    data object NavigateBack : EditAddressUiEffect
    data class ShowSnackBar(val message: String, val type: AlertType = AlertType.Error) : EditAddressUiEffect
}
