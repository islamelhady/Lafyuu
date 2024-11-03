package com.elhady.lafyuu.feature.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.core.database.repository.FavoritesRepository
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.cart.R
import com.elhady.lafyuu.feature.cart.domain.usecase.ApplyCouponUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.DecreaseCartItemUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.GetCartUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.IncreaseCartItemQuantityUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.RemoveCartItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
    private val applyCouponUseCase: ApplyCouponUseCase,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<CartUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        loadCart()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoritesRepository.observeFavoriteIds().collectLatest { ids ->
                _uiState.update { it.copy(favoriteProductIds = ids.toSet()) }
            }
        }
    }

    fun onEvent(event: CartUiEvent) {
        when (event) {
            CartUiEvent.LoadCart -> loadCart()
            CartUiEvent.RetryClicked -> loadCart()
            is CartUiEvent.IncreaseQuantity -> increaseQuantity(event.itemId)
            is CartUiEvent.DecreaseQuantity -> decreaseQuantity(event.itemId)
            is CartUiEvent.RemoveItem -> removeItem(event.itemId)
            is CartUiEvent.FavoriteClicked -> toggleFavorite(event.productId)
            is CartUiEvent.CouponCodeChanged -> {
                _uiState.update { it.copy(couponCodeInput = event.code, couponError = null) }
            }

            CartUiEvent.ApplyCouponClicked -> applyCoupon()
            CartUiEvent.CheckoutClicked -> sendEffect(CartUiEffect.NavigateToCheckout)
            is CartUiEvent.ProductClicked -> sendEffect(CartUiEffect.NavigateToProductDetails(event.productId))
            is CartUiEvent.BottomTabSelected -> sendEffect(CartUiEffect.NavigateToTab(event.tab.route))
        }
    }

    private fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            val isFavorite = _uiState.value.favoriteProductIds.contains(productId)
            if (isFavorite) {
                favoritesRepository.removeFavorite(productId)
                sendEffect(
                    CartUiEffect.ShowSnackbar(UiText.Resource(R.string.cart_removed_favorite), AlertType.Error)
                )
            } else {
                favoritesRepository.addFavorite(productId)
                sendEffect(
                    CartUiEffect.ShowSnackbar(UiText.Resource(R.string.cart_added_favorite), AlertType.Success)
                )
            }
        }
    }

    private fun loadCart(showLoading: Boolean = true) {
        loadJob?.cancel() // ضغطتين Retry ورا بعض ما يعملوش requests متوازية
        loadJob = viewModelScope.launch {
            if (showLoading) _uiState.update { it.copy(isLoading = true, error = null) }

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

                is AppResult.Failure -> {
                    val message = result.error.toUiText()
                    if (_uiState.value.cart == null) {
                        // مفيش داتا معروضة: الـ error يبقى State مع زرار Retry
                        _uiState.update { it.copy(isLoading = false, error = message) }
                    } else {
                        // فيه داتا معروضة: ما نمسحهاش، ننبّه بس
                        _uiState.update { it.copy(isLoading = false) }
                        sendEffect(CartUiEffect.ShowSnackbar(message, AlertType.Error))
                    }
                }
            }
        }
    }

    private fun increaseQuantity(itemId: String) =
        updateItem(itemId) { increaseCartItemQuantityUseCase(itemId) }

    private fun decreaseQuantity(itemId: String) =
        updateItem(itemId) { decreaseCartItemUseCase(itemId) }

    private fun removeItem(itemId: String) = updateItem(
        itemId = itemId,
        successMessage = UiText.Resource(R.string.cart_item_removed)
    ) { removeCartItemUseCase(itemId) }

    // الـ 3 عمليات كانوا نفس الهيكل بالظبط
    private fun updateItem(
        itemId: String,
        successMessage: UiText? = null,
        action: suspend () -> AppResult<Unit>
    ) {
        viewModelScope.launch {
            setUpdatingItem(itemId, true)
            when (val result = action()) {
                is AppResult.Success -> {
                    setUpdatingItem(itemId, false)
                    successMessage?.let {
                        sendEffect(CartUiEffect.ShowSnackbar(it, AlertType.Success))
                    }
                    loadCart(showLoading = false) // من غير spinner على الشاشة كلها
                }

                is AppResult.Failure -> {
                    setUpdatingItem(itemId, false)
                    sendEffect(CartUiEffect.ShowSnackbar(result.error.toUiText(), AlertType.Error))
                }
            }
        }
    }

    private fun applyCoupon() {
        val code = _uiState.value.couponCodeInput
        if (code.isBlank()) {
            _uiState.update { it.copy(couponError = UiText.Resource(R.string.cart_coupon_empty)) }
            return
        }

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
                        CartUiEffect.ShowSnackbar(UiText.Resource(R.string.cart_coupon_applied), AlertType.Success)
                    )
                }

                is AppResult.Failure -> {
                    // 400 هنا معناه كوبون غير صالح. المعنى ده خاص بالـ action دي، فمكانه الـ ViewModel
                    val message = when (result.error) {
                        is AppError.Validation -> UiText.Resource(R.string.cart_coupon_invalid)
                        else -> result.error.toUiText()
                    }
                    _uiState.update { it.copy(isApplyingCoupon = false, couponError = message) }
                    sendEffect(CartUiEffect.ShowSnackbar(message, AlertType.Error))
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
