package com.elhady.lafyuu.feature.checkout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.checkout.domain.model.Address
import com.elhady.lafyuu.feature.checkout.domain.usecase.DeleteAddressUseCase
import com.elhady.lafyuu.feature.checkout.domain.usecase.GetAddressesUseCase
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
class CheckoutViewModel @Inject constructor(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<CheckoutUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadAddresses()
    }

    fun onEvent(event: CheckoutUiEvent) {
        when (event) {
            CheckoutUiEvent.LoadAddresses -> loadAddresses()
            is CheckoutUiEvent.SelectAddress -> {
                _uiState.update { it.copy(selectedAddress = event.address) }
            }
            is CheckoutUiEvent.SelectPaymentMethod -> {
                _uiState.update { it.copy(selectedPaymentMethod = event.method) }
            }
            CheckoutUiEvent.OpenAddAddress -> {
                sendEffect(CheckoutUiEffect.NavigateToAddAddress)
            }
            is CheckoutUiEvent.OpenEditAddress -> {
                sendEffect(CheckoutUiEffect.NavigateToEditAddress(event.addressId))
            }
            is CheckoutUiEvent.DeleteAddress -> deleteAddress(event.addressId)
            CheckoutUiEvent.NextClicked -> proceedToPayment()
            CheckoutUiEvent.RetryClicked -> loadAddresses()
            CheckoutUiEvent.BackClicked -> sendEffect(CheckoutUiEffect.NavigateBack)
        }
    }

    fun loadAddresses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getAddressesUseCase()) {
                is AppResult.Success -> {
                    val list = result.data
                    val currentSelectedId = _uiState.value.selectedAddress?.id
                    val newlySelected = list.find { it.id == currentSelectedId } ?: list.lastOrNull()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            addresses = list,
                            selectedAddress = newlySelected,
                            error = null
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Failed to load addresses"
                        )
                    }
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = deleteAddressUseCase(addressId)) {
                is AppResult.Success -> {
                    _uiState.update { state ->
                        val updatedList = state.addresses.filter { it.id != addressId }
                        state.copy(
                            isLoading = false,
                            addresses = updatedList,
                            selectedAddress = if (state.selectedAddress?.id == addressId) updatedList.lastOrNull() else state.selectedAddress
                        )
                    }
                    sendEffect(CheckoutUiEffect.ShowSnackbar("Address deleted", AlertType.Success))
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEffect(CheckoutUiEffect.ShowSnackbar(result.message ?: "Failed to delete address", AlertType.Error))
                }
                is AppResult.Loading -> {}
            }
        }
    }

    private fun proceedToPayment() {
        val addressId = _uiState.value.selectedAddress?.id ?: run {
            sendEffect(CheckoutUiEffect.ShowSnackbar("Please select a shipping address", AlertType.Error))
            return
        }
        val paymentMethod = _uiState.value.selectedPaymentMethod
        sendEffect(CheckoutUiEffect.NavigateToPayment(addressId, paymentMethod))
    }

    private fun sendEffect(effect: CheckoutUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
