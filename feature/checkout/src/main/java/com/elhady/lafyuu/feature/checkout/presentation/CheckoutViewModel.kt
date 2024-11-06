package com.elhady.lafyuu.feature.checkout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.address.domain.usecase.DeleteAddressUseCase
import com.elhady.lafyuu.feature.address.domain.usecase.GetAddressesUseCase
import com.elhady.lafyuu.feature.checkout.R
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
                _uiState.update { it.copy(selectedAddressId = event.addressId) }
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
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            addresses = list,
                            selectedAddressId = it.selectedAddressId ?: list.firstOrNull()?.id,
                            error = null
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.toUiText()
                        )
                    }
                }
            }
        }
    }

    private fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            when (val result = deleteAddressUseCase(addressId)) {
                is AppResult.Success -> {
                    sendEffect(CheckoutUiEffect.ShowSnackbar(message = UiText.Resource(R.string.address_deleted), type = AlertType.Success))
                    loadAddresses()
                }

                is AppResult.Failure -> {
                    val errorMessage = result.error.toUiText()
                    sendEffect(
                        CheckoutUiEffect.ShowSnackbar(
                            message = errorMessage, type = AlertType.Error
                        )
                    )
                }
            }
        }
    }

    private fun proceedToPayment() {
        val addressId = _uiState.value.selectedAddressId ?: run {
            sendEffect(
                CheckoutUiEffect.ShowSnackbar(
                    message = UiText.Resource(R.string.please_select_shipping_address),
                    type = AlertType.Error
                )
            )
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
