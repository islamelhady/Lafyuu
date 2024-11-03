package com.elhady.lafyuu.feature.address.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText

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
    val stateError: UiText? = null,
    val cityError: UiText? = null,
    val streetError: UiText? = null,
    val apartmentError: UiText? = null,
    val phoneError: UiText? = null,
    val error: UiText? = null
)

sealed interface AddAddressUiEvent {
    data class FieldChanged(val field: AddressField, val value: String) : AddAddressUiEvent
    data object AddAddressClicked : AddAddressUiEvent
    data object BackClicked : AddAddressUiEvent
}

sealed interface AddAddressUiEffect {
    data object NavigateBack : AddAddressUiEffect
    data class ShowSnackBar(val message: UiText, val type: AlertType = AlertType.Error) : AddAddressUiEffect
}
