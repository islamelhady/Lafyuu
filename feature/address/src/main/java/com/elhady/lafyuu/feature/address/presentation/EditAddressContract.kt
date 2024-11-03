package com.elhady.lafyuu.feature.address.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText

data class EditAddressUiState(
    val addressId: String = "",
    val state: String = "",
    val city: String = "",
    val street: String = "",
    val apartment: String = "",
    val phoneNumber: String = "",
    val notes: String = "",
    val isFetching: Boolean = false,
    val isLoading: Boolean = false,
    val stateError: UiText? = null,
    val cityError: UiText? = null,
    val streetError: UiText? = null,
    val apartmentError: UiText? = null,
    val phoneError: UiText? = null,
    val error: UiText? = null
)

sealed interface EditAddressUiEvent {
    data class LoadAddress(val addressId: String) : EditAddressUiEvent
    data class FieldChanged(val field: AddressField, val value: String) : EditAddressUiEvent
    data object UpdateAddressClicked : EditAddressUiEvent
    data object BackClicked : EditAddressUiEvent
}

sealed interface EditAddressUiEffect {
    data object NavigateBack : EditAddressUiEffect
    data class ShowSnackBar(val message: UiText, val type: AlertType = AlertType.Error) : EditAddressUiEffect
}
