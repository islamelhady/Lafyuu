package com.elhady.lafyuu.feature.checkout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.checkout.domain.usecase.CreateAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val createAddressUseCase: CreateAddressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAddressUiState())
    val uiState: StateFlow<AddAddressUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<AddAddressUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: AddAddressUiEvent) {
        when (event) {
            is AddAddressUiEvent.FieldChanged -> updateField(event.field, event.value)
            AddAddressUiEvent.AddAddressClicked -> createAddress()
            AddAddressUiEvent.BackClicked -> sendEffect(AddAddressUiEffect.NavigateBack)
        }
    }

    private fun updateField(field: AddressField, value: String) {
        _uiState.update { state ->
            when (field) {
                AddressField.STATE -> state.copy(state = value, stateError = null)
                AddressField.CITY -> state.copy(city = value, cityError = null)
                AddressField.STREET -> state.copy(street = value, streetError = null)
                AddressField.APARTMENT -> state.copy(apartment = value, apartmentError = null)
                AddressField.PHONE -> state.copy(phoneNumber = value, phoneError = null)
                AddressField.NOTES -> state.copy(notes = value)
            }
        }
    }

    private fun createAddress() {
        val state = _uiState.value.state
        val city = _uiState.value.city
        val street = _uiState.value.street
        val apartment = _uiState.value.apartment
        val phoneNumber = _uiState.value.phoneNumber
        val notes = _uiState.value.notes

        val stateError = if (state.isBlank()) "Please Fill The Form" else null
        val cityError = if (city.isBlank()) "Please Fill The Form" else null
        val streetError = if (street.isBlank()) "Please Fill The Form" else null
        val aptError = if (apartment.isBlank()) "Please Fill The Form" else null
        val phoneError = if (phoneNumber.isBlank()) "Please Fill The Form" else null

        if (stateError != null || cityError != null || streetError != null || aptError != null || phoneError != null) {
            _uiState.update {
                it.copy(
                    stateError = stateError,
                    cityError = cityError,
                    streetError = streetError,
                    apartmentError = aptError,
                    phoneError = phoneError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = createAddressUseCase(state, city, street, apartment, phoneNumber, notes)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEffect(AddAddressUiEffect.NavigateBack)
                }

                is AppResult.Error -> {
                    val msg = result.message ?: "Failed to create address"
                    _uiState.update { it.copy(isLoading = false, error = msg) }
                    sendEffect(AddAddressUiEffect.ShowSnackBar(msg, AlertType.Error))
                }

                is AppResult.Loading -> {}
            }
        }
    }

    private fun sendEffect(effect: AddAddressUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
