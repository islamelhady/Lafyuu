package com.elhady.lafyuu.feature.address.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.address.R
import com.elhady.lafyuu.feature.address.domain.usecase.GetAddressUseCase
import com.elhady.lafyuu.feature.address.domain.usecase.UpdateAddressUseCase
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
class EditAddressViewModel @Inject constructor(
    private val getAddressUseCase: GetAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val addressIdArg: String = savedStateHandle["addressId"] ?: ""

    private val _uiState = MutableStateFlow(EditAddressUiState(addressId = addressIdArg))
    val uiState: StateFlow<EditAddressUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<EditAddressUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        if (addressIdArg.isNotBlank()) {
            loadAddress(addressIdArg)
        }
    }

    fun onEvent(event: EditAddressUiEvent) {
        when (event) {
            is EditAddressUiEvent.LoadAddress -> loadAddress(event.addressId)
            is EditAddressUiEvent.FieldChanged -> updateField(event.field, event.value)
            EditAddressUiEvent.UpdateAddressClicked -> updateAddress()
            EditAddressUiEvent.BackClicked -> sendEffect(EditAddressUiEffect.NavigateBack)
        }
    }

    private fun loadAddress(addressId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetching = true, error = null) }
            when (val result = getAddressUseCase(addressId)) {
                is AppResult.Success -> {
                    val addr = result.data
                    _uiState.update {
                        it.copy(
                            isFetching = false,
                            addressId = addr.id,
                            state = addr.state,
                            city = addr.city,
                            street = addr.street,
                            apartment = addr.apartment,
                            phoneNumber = addr.phoneNumber,
                            notes = addr.notes,
                            error = null
                        )
                    }
                }
                is AppResult.Failure -> {
                    val errorMessage = result.error.toUiText()
                    _uiState.update { it.copy(isFetching = false, error = errorMessage) }
                    sendEffect(EditAddressUiEffect.ShowSnackBar(errorMessage, AlertType.Error))
                }
            }
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

    private fun updateAddress() {
        val id = _uiState.value.addressId
        val state = _uiState.value.state
        val city = _uiState.value.city
        val street = _uiState.value.street
        val apartment = _uiState.value.apartment
        val phoneNumber = _uiState.value.phoneNumber
        val notes = _uiState.value.notes

        val stateError = if (state.isBlank()) UiText.Resource(R.string.error_empty_field) else null
        val cityError = if (city.isBlank()) UiText.Resource(R.string.error_empty_field) else null
        val streetError = if (street.isBlank()) UiText.Resource(R.string.error_empty_field) else null
        val apartmentError = if (apartment.isBlank()) UiText.Resource(R.string.error_empty_field) else null
        val phoneNumberError = if (phoneNumber.isBlank()) UiText.Resource(R.string.error_empty_field) else null

        if (stateError != null || cityError != null || streetError != null || apartmentError != null || phoneNumberError != null) {
            _uiState.update {
                it.copy(
                    stateError = stateError,
                    cityError = cityError,
                    streetError = streetError,
                    apartmentError = apartmentError,
                    phoneError = phoneNumberError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = updateAddressUseCase(id, state, city, street, apartment, phoneNumber, notes)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEffect(EditAddressUiEffect.NavigateBack)
                }
                is AppResult.Failure -> {
                    val errorMessage = result.error.toUiText()
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                    sendEffect(EditAddressUiEffect.ShowSnackBar(message = errorMessage, type = AlertType.Error))
                }
            }
        }
    }

    private fun sendEffect(effect: EditAddressUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
