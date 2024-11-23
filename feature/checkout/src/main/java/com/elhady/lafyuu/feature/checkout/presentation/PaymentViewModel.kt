package com.elhady.lafyuu.feature.checkout.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.checkout.R
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutCartGateway
import com.elhady.lafyuu.feature.checkout.domain.usecase.CheckoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val checkoutUseCase: CheckoutUseCase,
    private val cartGateway: CheckoutCartGateway,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val shippingAddressId: String =
        savedStateHandle.get<String>(SHIPPING_ADDRESS_ID_ARG).orEmpty()

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PaymentUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<PaymentUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadSummary()
    }

    fun onIntent(intent: PaymentUiIntent) {
        when (intent) {
            is PaymentUiIntent.MethodSelected -> _uiState.update {
                if (it.stage == PaymentStage.Selecting) it.copy(selectedMethod = intent.method) else it
            }

            PaymentUiIntent.PayClicked -> pay()
            PaymentUiIntent.RetryClicked -> loadSummary()
            PaymentUiIntent.UrlOpened -> _uiState.update { it.copy(urlToOpen = null) }
            PaymentUiIntent.OpenPaymentPageClicked -> _uiState.update {
                val stage = it.stage
                if (stage is PaymentStage.AwaitingPayment) it.copy(urlToOpen = stage.paymentUrl) else it
            }

            PaymentUiIntent.ViewOrdersClicked -> sendEffect(PaymentUiEffect.NavigateToOrders)
            PaymentUiIntent.BackClicked -> sendEffect(PaymentUiEffect.NavigateBack)
        }
    }

    private fun loadSummary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loadError = null) }
            when (val result = cartGateway.getOrderSummary()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        total = result.data.total,
                        couponCode = result.data.couponCode
                    )
                }

                // without a total the user must not pay, so this is a full screen error
                is AppResult.Failure -> _uiState.update {
                    it.copy(isLoading = false, loadError = result.error.toUiText())
                }
            }
        }
    }

    private fun pay() {
        val state = _uiState.value
        if (state.isCheckingOut || state.isLoading || state.loadError != null) return
        if (state.stage != PaymentStage.Selecting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            val result = checkoutUseCase(shippingAddressId, state.selectedMethod, state.couponCode)
            when (result) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isCheckingOut = false) }.also {
                        applyCheckoutResult(result.data)
                    }
                    // the order exists now, the shared cart (screen + tab badge) must follow
                    cartGateway.refreshCart()
                }

                is AppResult.Failure -> {
                    _uiState.update { it.copy(isCheckingOut = false) }
                    _uiEffect.emit(
                        PaymentUiEffect.ShowSnackbar(result.error.toUiText(), AlertType.Error)
                    )
                }
            }
        }
    }

    private fun applyCheckoutResult(result: CheckoutResult) {
        _uiState.update { state ->
            when (result) {
                is CheckoutResult.Completed -> state.copy(
                    stage = PaymentStage.Completed(
                        result.message.takeIf { it.isNotBlank() }?.let { UiText.Dynamic(it) }
                            ?: UiText.Resource(R.string.payment_success_default)
                    )
                )

                is CheckoutResult.PaymentRequired -> state.copy(
                    stage = PaymentStage.AwaitingPayment(result.paymentUrl),
                    urlToOpen = result.paymentUrl
                )
            }
        }
    }

    private fun sendEffect(effect: PaymentUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }

    companion object {
        const val SHIPPING_ADDRESS_ID_ARG = "shippingAddressId"
    }
}
