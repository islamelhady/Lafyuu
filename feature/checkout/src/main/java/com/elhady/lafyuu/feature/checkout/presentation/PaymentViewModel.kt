package com.elhady.lafyuu.feature.checkout.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.cart.domain.usecase.GetCartUseCase
import com.elhady.lafyuu.feature.checkout.domain.usecase.CheckoutUseCase
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
class PaymentViewModel @Inject constructor(
    private val checkoutUseCase: CheckoutUseCase,
    private val getCartUseCase: GetCartUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val shippingAddressIdArg: String = savedStateHandle["shippingAddressId"] ?: ""
    private val paymentMethodArg: String = savedStateHandle["paymentMethod"] ?: "Credit Card"

    private val _uiState = MutableStateFlow(
        PaymentUiState(
            shippingAddressId = shippingAddressIdArg,
            selectedPaymentMethod = paymentMethodArg
        )
    )
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<PaymentUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadCheckoutTotal()
    }

    fun onEvent(event: PaymentUiEvent) {
        when (event) {
            is PaymentUiEvent.SelectPaymentMethod -> {
                _uiState.update { it.copy(selectedPaymentMethod = event.method) }
            }
            is PaymentUiEvent.PayClicked -> executeCheckout(event.couponCode)
            PaymentUiEvent.DismissSuccess -> {
                _uiState.update { it.copy(isSuccess = false) }
                sendEffect(PaymentUiEffect.NavigateBack)
            }
            PaymentUiEvent.BackClicked -> sendEffect(PaymentUiEffect.NavigateBack)
        }
    }

    private fun loadCheckoutTotal() {
        viewModelScope.launch {
            when (val result = getCartUseCase()) {
                is AppResult.Success -> {
                    val cart = result.data
                    val total = cart.finalTotal ?: cart.itemsTotal
                    _uiState.update { it.copy(checkoutTotal = total) }
                }
                else -> {}
            }
        }
    }

    private fun executeCheckout(couponCode: String?) {
        if (_uiState.value.isCheckingOut) return // Prevent duplicate submissions

        val addressId = _uiState.value.shippingAddressId
        val paymentMethod = _uiState.value.selectedPaymentMethod

        if (addressId.isBlank()) {
            sendEffect(PaymentUiEffect.ShowSnackBar("Shipping address is missing", AlertType.Error))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            when (val result = checkoutUseCase(addressId, paymentMethod, couponCode)) {
                is AppResult.Success -> {
                    val res = result.data
                    _uiState.update {
                        it.copy(
                            isCheckingOut = false,
                            checkoutResult = res,
                            isSuccess = true
                        )
                    }
                    res.unifiedCheckoutUrl?.let { url ->
                        sendEffect(PaymentUiEffect.OpenExternalUrl(url))
                    }
                    sendEffect(PaymentUiEffect.ShowSnackBar(res.message.ifBlank { "Checkout successful!" }, AlertType.Success))
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isCheckingOut = false) }
                    sendEffect(PaymentUiEffect.ShowSnackBar(result.message ?: "Checkout failed", AlertType.Error))
                }
                is AppResult.Loading -> {}
            }
        }
    }

    private fun sendEffect(effect: PaymentUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
