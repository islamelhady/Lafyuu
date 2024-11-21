package com.elhady.lafyuu.feature.address.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.model.AddressField
import com.elhady.lafyuu.feature.address.domain.model.AddressInput

data class AddressForm(
    val state: String = "",
    val city: String = "",
    val street: String = "",
    val apartment: String = "",
    val phoneNumber: String = "",
    val notes: String = ""
) {
    fun valueOf(field: AddressField): String = when (field) {
        AddressField.STATE -> state
        AddressField.CITY -> city
        AddressField.STREET -> street
        AddressField.APARTMENT -> apartment
        AddressField.PHONE -> phoneNumber
        AddressField.NOTES -> notes
    }

    fun update(field: AddressField, value: String): AddressForm = when (field) {
        AddressField.STATE -> copy(state = value)
        AddressField.CITY -> copy(city = value)
        AddressField.STREET -> copy(street = value)
        AddressField.APARTMENT -> copy(apartment = value)
        AddressField.PHONE -> copy(phoneNumber = value)
        AddressField.NOTES -> copy(notes = value)
    }

    fun toInput() = AddressInput(
        state = state,
        city = city,
        street = street,
        apartment = apartment,
        phoneNumber = phoneNumber,
        notes = notes
    )
}

fun Address.toForm() = AddressForm(
    state = state,
    city = city,
    street = street,
    apartment = apartment,
    phoneNumber = phoneNumber,
    notes = notes
)

data class AddressFormUiState(
    val isEditMode: Boolean = false,
    val isFetching: Boolean = false,
    val isSaving: Boolean = false,
    val form: AddressForm = AddressForm(),
    val errors: Map<AddressField, UiText> = emptyMap(),
    val loadError: UiText? = null
)

sealed interface AddressFormUiIntent {
    data class FieldChanged(val field: AddressField, val value: String) : AddressFormUiIntent
    data object SaveClicked : AddressFormUiIntent
    data object RetryLoadClicked : AddressFormUiIntent
    data object BackClicked : AddressFormUiIntent
}

sealed interface AddressFormUiEffect {
    data object NavigateBack : AddressFormUiEffect
    data class ShowSnackbar(
        val message: UiText,
        val type: AlertType = AlertType.Error
    ) : AddressFormUiEffect
}