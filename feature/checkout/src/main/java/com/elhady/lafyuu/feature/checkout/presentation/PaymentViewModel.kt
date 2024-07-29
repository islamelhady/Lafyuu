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
            shippingAddressId = shippingAddressIdArg
        )
    )
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<PaymentUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadCartAndCoupon()
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

    private fun loadCartAndCoupon() {
        viewModelScope.launch {
            when (val result = getCartUseCase()) {
                is AppResult.Success -> {
                    val cart = result.data
                    val total = cart.finalTotal ?: cart.itemsTotal
                    val coupon = cart.appliedCoupon?.code
                    _uiState.update { it.copy(checkoutTotal = total, couponCode = coupon) }
                }
                else -> {}
            }
        }
    }

    private fun executeCheckout(customCouponCode: String?) {
        if (_uiState.value.isCheckingOut) return

        val addressId = _uiState.value.shippingAddressId
        val paymentMethod = _uiState.value.selectedPaymentMethod
        val couponCode = customCouponCode ?: _uiState.value.couponCode

        if (addressId.isBlank()) {
            sendEffect(PaymentUiEffect.ShowSnackBar("Shipping address is missing", AlertType.Error))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            when (val result = checkoutUseCase(addressId, paymentMethod, couponCode)) {
                is AppResult.Success -> {
                    val res = result.data
                    val hasExternalFlow = !res.unifiedCheckoutUrl.isNullOrBlank() || !res.paymentClientSecret.isNullOrBlank()

                    _uiState.update {
                        it.copy(
                            isCheckingOut = false,
                            checkoutResult = res,
                            isSuccess = !hasExternalFlow
                        )
                    }
                    res.unifiedCheckoutUrl?.let { url ->
                        sendEffect(PaymentUiEffect.OpenExternalUrl(url))
                    }
                    val msg = res.message.ifBlank { if (hasExternalFlow) "Please complete payment" else "Checkout successful!" }
                    sendEffect(PaymentUiEffect.ShowSnackBar(msg, if (hasExternalFlow) AlertType.Warning else AlertType.Success))
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
