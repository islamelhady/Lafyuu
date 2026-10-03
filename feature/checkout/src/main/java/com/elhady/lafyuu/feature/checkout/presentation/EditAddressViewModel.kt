package com.elhady.lafyuu.feature.checkout.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.checkout.domain.usecase.GetAddressUseCase
import com.elhady.lafyuu.feature.checkout.domain.usecase.UpdateAddressUseCase
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
                is AppResult.Error -> {
                    val msg = result.message ?: "Failed to load address"
                    _uiState.update { it.copy(isFetching = false, error = msg) }
                    sendEffect(EditAddressUiEffect.ShowSnackbar(msg, AlertType.Error))
                }
                is AppResult.Loading -> {}
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
        val s = _uiState.value.state
        val c = _uiState.value.city
        val st = _uiState.value.street
        val apt = _uiState.value.apartment
        val phone = _uiState.value.phoneNumber
        val notes = _uiState.value.notes

        val stateErr = if (s.isBlank()) "Please Fill The Form" else null
        val cityErr = if (c.isBlank()) "Please Fill The Form" else null
        val streetErr = if (st.isBlank()) "Please Fill The Form" else null
        val aptErr = if (apt.isBlank()) "Please Fill The Form" else null
        val phoneErr = if (phone.isBlank()) "Please Fill The Form" else null

        if (stateErr != null || cityErr != null || streetErr != null || aptErr != null || phoneErr != null) {
            _uiState.update {
                it.copy(
                    stateError = stateErr,
                    cityError = cityErr,
                    streetError = streetErr,
                    apartmentError = aptErr,
                    phoneError = phoneErr
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = updateAddressUseCase(id, s, c, st, apt, phone, notes)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEffect(EditAddressUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    val msg = result.message ?: "Failed to update address"
                    _uiState.update { it.copy(isLoading = false, error = msg) }
                    sendEffect(EditAddressUiEffect.ShowSnackbar(msg, AlertType.Error))
                }
                is AppResult.Loading -> {}
            }
        }
    }

    private fun sendEffect(effect: EditAddressUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
