package com.elhady.lafyuu.feature.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.cart.R
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.repository.CartFavoritesGateway
import com.elhady.lafyuu.feature.cart.domain.usecase.ApplyCouponUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.DecreaseCartItemUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.GetCartUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.IncreaseCartItemQuantityUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.ObserveCartUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.RemoveCartItemUseCase
import com.elhady.lafyuu.feature.cart.presentation.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val observeCartUseCase: ObserveCartUseCase,
    private val increaseCartItemQuantityUseCase: IncreaseCartItemQuantityUseCase,
    private val decreaseCartItemUseCase: DecreaseCartItemUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val applyCouponUseCase: ApplyCouponUseCase,
    private val favoritesGateway: CartFavoritesGateway
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CartUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<CartUiEffect> = _uiEffect.asSharedFlow()

    private var loadJob: Job? = null

    // Domain copy of the latest cart, needed to build the favorite snapshot
    private var cartSnapshot: Cart? = null

    init {
        observeCart()
        observeFavorites()
        loadCart()
    }

    fun onIntent(intent: CartUiIntent) {
        when (intent) {
            CartUiIntent.RetryClicked -> loadCart()
            is CartUiIntent.IncreaseQuantity ->
                updateItem(intent.itemId) { increaseCartItemQuantityUseCase(intent.itemId) }
            is CartUiIntent.DecreaseQuantity ->
                updateItem(intent.itemId) { decreaseCartItemUseCase(intent.itemId) }
            is CartUiIntent.RemoveItem -> updateItem(
                itemId = intent.itemId,
                successMessage = UiText.Resource(R.string.cart_item_removed)
            ) { removeCartItemUseCase(intent.itemId) }

            is CartUiIntent.FavoriteClicked -> toggleFavorite(intent.productId)
            is CartUiIntent.CouponCodeChanged ->
                _uiState.update { it.copy(couponCodeInput = intent.code, couponError = null) }
            CartUiIntent.ApplyCouponClicked -> applyCoupon()
            CartUiIntent.CheckoutClicked -> sendEffect(CartUiEffect.NavigateToCheckout)
            is CartUiIntent.ProductClicked ->
                sendEffect(CartUiEffect.NavigateToProductDetails(intent.productId))
            is CartUiIntent.BottomTabSelected ->
                sendEffect(CartUiEffect.NavigateToTab(intent.tab.route))
        }
    }

    // The repository keeps its cache in sync after every mutation, so this is the single
    // way the cart reaches the UI after the first load.
    private fun observeCart() {
        observeCartUseCase()
            .onEach { cart ->
                cartSnapshot = cart
                _uiState.update { it.copy(cart = cart?.toUi()) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFavorites() {
        favoritesGateway.observeFavoriteProductIds()
            .onEach { ids -> _uiState.update { it.copy(favoriteProductIds = ids) } }
            .launchIn(viewModelScope)
    }

    private fun loadCart() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getCartUseCase()) {
                is AppResult.Success -> {
                    // also set directly so isLoading never flips before the cart is in the state
                    cartSnapshot = result.data
                    _uiState.update {
                        it.copy(isLoading = false, cart = result.data.toUi(), error = null)
                    }
                }

                is AppResult.Failure -> {
                    val message = result.error.toUiText()
                    if (_uiState.value.cart == null) {
                        _uiState.update { it.copy(isLoading = false, error = message) }
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                        showSnackbar(message, AlertType.Error)
                    }
                }
            }
        }
    }

    private fun updateItem(
        itemId: String,
        successMessage: UiText? = null,
        action: suspend () -> AppResult<Unit>
    ) {
        if (itemId in _uiState.value.updatingItemIds) return
        viewModelScope.launch {
            setUpdatingItem(itemId, true)
            val result = action()
            setUpdatingItem(itemId, false)
            when (result) {
                is AppResult.Success ->
                    successMessage?.let { showSnackbar(it, AlertType.Success) }
                is AppResult.Failure ->
                    showSnackbar(result.error.toUiText(), AlertType.Error)
            }
        }
    }

    private fun applyCoupon() {
        val state = _uiState.value
        if (state.isApplyingCoupon) return
        if (state.couponCodeInput.isBlank()) {
            _uiState.update { it.copy(couponError = UiText.Resource(R.string.cart_coupon_empty)) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isApplyingCoupon = true, couponError = null) }
            when (val result = applyCouponUseCase(state.couponCodeInput)) {
                is AppResult.Success -> {
                    cartSnapshot = result.data
                    _uiState.update {
                        it.copy(isApplyingCoupon = false, cart = result.data.toUi())
                    }
                    showSnackbar(UiText.Resource(R.string.cart_coupon_applied), AlertType.Success)
                }

                is AppResult.Failure -> {
                    if (result.error is AppError.Validation) {
                        // invalid coupon: shown inline under the text field
                        _uiState.update {
                            it.copy(
                                isApplyingCoupon = false,
                                couponError = UiText.Resource(R.string.cart_coupon_invalid)
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isApplyingCoupon = false) }
                        showSnackbar(result.error.toUiText(), AlertType.Error)
                    }
                }
            }
        }
    }

    private fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            if (productId in _uiState.value.favoriteProductIds) {
                favoritesGateway.removeFavorite(productId)
                showSnackbar(UiText.Resource(R.string.cart_removed_favorite), AlertType.Success)
            } else {
                val item = cartSnapshot?.items?.firstOrNull { it.productId == productId }
                    ?: return@launch
                favoritesGateway.addFavorite(item)
                showSnackbar(UiText.Resource(R.string.cart_added_favorite), AlertType.Success)
            }
        }
    }

    private fun setUpdatingItem(itemId: String, updating: Boolean) {
        _uiState.update { state ->
            state.copy(
                updatingItemIds = if (updating) state.updatingItemIds + itemId
                else state.updatingItemIds - itemId
            )
        }
    }

    private fun sendEffect(effect: CartUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }

    private suspend fun showSnackbar(message: UiText, type: AlertType) {
        _uiEffect.emit(CartUiEffect.ShowSnackbar(message, type))
    }
}