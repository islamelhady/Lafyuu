package com.elhady.lafyuu.feature.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.cart.domain.usecase.ApplyCouponUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.DecreaseCartItemUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.GetCartUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.IncreaseCartItemQuantityUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.RemoveCartItemUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.UpdateCartItemQuantityUseCase
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
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val increaseCartItemQuantityUseCase: IncreaseCartItemQuantityUseCase,
    private val decreaseCartItemUseCase: DecreaseCartItemUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val applyCouponUseCase: ApplyCouponUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<CartUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadCart()
    }

    fun onEvent(event: CartUiEvent) {
        when (event) {
            CartUiEvent.LoadCart -> loadCart()
            is CartUiEvent.IncreaseQuantity -> increaseQuantity(event.itemId)
            is CartUiEvent.DecreaseQuantity -> decreaseQuantity(event.itemId)
            is CartUiEvent.RemoveItem -> removeItem(event.itemId)
            is CartUiEvent.CouponCodeChanged -> {
                _uiState.update { it.copy(couponCodeInput = event.code, couponError = null) }
            }

            CartUiEvent.ApplyCouponClicked -> applyCoupon()
            CartUiEvent.CheckoutClicked -> sendEffect(CartUiEffect.NavigateToCheckout)
            CartUiEvent.RetryClicked -> loadCart()
            is CartUiEvent.ProductClicked -> sendEffect(CartUiEffect.NavigateToProductDetails(event.productId))
            is CartUiEvent.BottomTabSelected -> sendEffect(CartUiEffect.NavigateToTab(event.tab.route))
        }
    }

    private fun loadCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getCartUseCase()) {
                is AppResult.Success -> {
                    val cart = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            cart = cart,
                            originalTotal = cart.originalTotal ?: cart.itemsTotal,
                            finalTotal = cart.finalTotal ?: cart.itemsTotal,
                            error = null
                        )
                    }
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Failed to load cart"
                        )
                    }
                }

                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun increaseQuantity(itemId: String) {
        viewModelScope.launch {
            setUpdatingItem(itemId, true)
            when (val result = increaseCartItemQuantityUseCase(itemId)) {
                is AppResult.Success -> {
                    setUpdatingItem(itemId, false)
                    loadCart()
                }

                is AppResult.Error -> {
                    setUpdatingItem(itemId, false)
                    sendEffect(
                        CartUiEffect.ShowSnackbar(
                            message = result.message ?: "Failed to increase quantity",
                            type = AlertType.Error
                        )
                    )
                }

                is AppResult.Loading -> {}
            }
        }
    }

    private fun decreaseQuantity(itemId: String) {
        viewModelScope.launch {
            setUpdatingItem(itemId, true)
            when (val result = decreaseCartItemUseCase(itemId)) {
                is AppResult.Success -> {
                    setUpdatingItem(itemId, false)
                    loadCart()
                }

                is AppResult.Error -> {
                    setUpdatingItem(itemId, false)
                    sendEffect(
                        CartUiEffect.ShowSnackbar(
                            result.message ?: "Failed to decrease quantity", AlertType.Error
                        )
                    )
                }

                is AppResult.Loading -> {}
            }
        }
    }

    private fun removeItem(itemId: String) {
        viewModelScope.launch {
            setUpdatingItem(itemId, true)
            when (val result = removeCartItemUseCase(itemId)) {
                is AppResult.Success -> {
                    setUpdatingItem(itemId, false)
                    sendEffect(
                        CartUiEffect.ShowSnackbar(
                            "Item removed from cart",
                            AlertType.Success
                        )
                    )
                    loadCart()
                }

                is AppResult.Error -> {
                    setUpdatingItem(itemId, false)
                    sendEffect(
                        CartUiEffect.ShowSnackbar(
                            result.message ?: "Failed to remove item",
                            AlertType.Error
                        )
                    )
                }

                is AppResult.Loading -> {}
            }
        }
    }

    private fun applyCoupon() {
        val code = _uiState.value.couponCodeInput
        if (code.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isApplyingCoupon = true, couponError = null) }
            when (val result = applyCouponUseCase(code)) {
                is AppResult.Success -> {
                    val summary = result.data
                    _uiState.update {
                        it.copy(
                            isApplyingCoupon = false,
                            originalTotal = summary.originalTotal ?: summary.itemsTotal,
                            discountAmount = summary.discountAmount,
                            finalTotal = summary.finalTotal ?: summary.itemsTotal,
                            appliedCouponCode = summary.appliedCoupon?.code ?: code,
                            couponError = null
                        )
                    }
                    sendEffect(
                        CartUiEffect.ShowSnackbar(
                            "Coupon applied successfully!",
                            AlertType.Success
                        )
                    )
                }

                is AppResult.Error -> {
                    val errMsg = result.message ?: "Your Cupon Is Not Correct"
                    _uiState.update {
                        it.copy(
                            isApplyingCoupon = false,
                            couponError = errMsg
                        )
                    }
                    sendEffect(CartUiEffect.ShowSnackbar(errMsg, AlertType.Error))
                }

                is AppResult.Loading -> {
                    _uiState.update { it.copy(isApplyingCoupon = true) }
                }
            }
        }
    }

    private fun setUpdatingItem(itemId: String, updating: Boolean) {
        _uiState.update { state ->
            val set = state.updatingItemIds.toMutableSet()
            if (updating) set.add(itemId) else set.remove(itemId)
            state.copy(updatingItemIds = set)
        }
    }

    private fun sendEffect(effect: CartUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
