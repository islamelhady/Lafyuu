package com.elhady.lafyuu.feature.address.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.address.domain.usecase.DeleteAddressUseCase
import com.elhady.lafyuu.feature.address.domain.usecase.GetAddressesUseCase
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
class AddressViewModel @Inject constructor(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<AddressUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadAddresses()
    }

    fun onEvent(event: AddressUiEvent) {
        when (event) {
            AddressUiEvent.LoadAddresses -> loadAddresses()
            is AddressUiEvent.AddressSelected -> {
                _uiState.update { it.copy(selectedAddressId = event.addressId) }
            }
            is AddressUiEvent.EditAddressClicked -> sendEffect(AddressUiEffect.NavigateToEditAddress(event.addressId))
            is AddressUiEvent.DeleteAddressClicked -> deleteAddress(event.addressId)
            AddressUiEvent.AddAddressClicked -> sendEffect(AddressUiEffect.NavigateToAddAddress)
            AddressUiEvent.BackClicked -> sendEffect(AddressUiEffect.NavigateBack)
        }
    }

    fun loadAddresses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getAddressesUseCase()) {
                is AppResult.Success -> {
                    val list = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            addresses = list,
                            selectedAddressId = it.selectedAddressId ?: list.firstOrNull()?.id,
                            error = null
                        )
                    }
                }
                is AppResult.Error -> {
                    val msg = result.message ?: "Failed to load addresses"
                    _uiState.update { it.copy(isLoading = false, error = msg) }
                    sendEffect(AddressUiEffect.ShowSnackbar(msg, AlertType.Error))
                }
                is AppResult.Loading -> {}
            }
        }
    }

    private fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            when (val result = deleteAddressUseCase(addressId)) {
                is AppResult.Success -> {
                    sendEffect(AddressUiEffect.ShowSnackbar("Address deleted successfully", AlertType.Success))
                    loadAddresses()
                }
                is AppResult.Error -> {
                    val msg = result.message ?: "Failed to delete address"
                    sendEffect(AddressUiEffect.ShowSnackbar(msg, AlertType.Error))
                }
                is AppResult.Loading -> {}
            }
        }
    }

    private fun sendEffect(effect: AddressUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
