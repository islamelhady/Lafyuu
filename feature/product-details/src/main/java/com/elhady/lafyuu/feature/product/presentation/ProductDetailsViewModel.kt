package com.elhady.lafyuu.feature.product.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.usecase.GetProductDetailsUseCase
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.product.R
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.repository.ProductCartGateway
import com.elhady.lafyuu.feature.product.domain.repository.ProductFavoriteGateway
import com.elhady.lafyuu.feature.product.domain.usecase.AddToCartUseCase
import com.elhady.lafyuu.feature.product.presentation.mapper.toUi
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
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val cartGateway: ProductCartGateway,
    private val favoriteGateway: ProductFavoriteGateway,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = savedStateHandle.get<String>(PRODUCT_ID_ARG).orEmpty()

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ProductDetailsUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<ProductDetailsUiEffect> = _uiEffect.asSharedFlow()

    private var loadedProduct: ProductDetails? = null
    private var loadJob: Job? = null

    init {
        if (productId.isBlank()) {
            _uiState.update {
                it.copy(isLoading = false, error = UiText.Resource(R.string.product_error_invalid_id))
            }
        } else {
            observeFavorite()
            observeCart()
            loadProduct()
            refreshCart()
        }
    }

    private fun refreshCart() {
        viewModelScope.launch { cartGateway.refresh() }
    }

    fun onIntent(intent: ProductDetailsUiIntent) {
        when (intent) {
            ProductDetailsUiIntent.FavoriteToggled -> toggleFavorite()
            ProductDetailsUiIntent.AddToCartClicked -> onAddToCartActionClicked()
            ProductDetailsUiIntent.RetryClicked -> loadProduct()
            ProductDetailsUiIntent.BackClicked -> sendEffect(ProductDetailsUiEffect.NavigateBack)
            ProductDetailsUiIntent.SearchClicked -> sendEffect(ProductDetailsUiEffect.NavigateToSearch)
            ProductDetailsUiIntent.SeeMoreReviewsClicked ->
                sendEffect(ProductDetailsUiEffect.NavigateToReviews(productId))
            ProductDetailsUiIntent.WriteReviewClicked ->
                sendEffect(ProductDetailsUiEffect.NavigateToWriteReview(productId))
            is ProductDetailsUiIntent.RecommendedProductClicked ->
                sendEffect(ProductDetailsUiEffect.NavigateToProductDetails(intent.productId))
        }
    }

    private fun observeFavorite() {
        favoriteGateway.isFavorite(productId)
            .onEach { isFavorite -> _uiState.update { it.copy(isFavorite = isFavorite) } }
            .launchIn(viewModelScope)
    }

    private fun observeCart() {
        cartGateway.observeCartItemId(productId)
            .onEach { itemId -> _uiState.update { it.copy(cartItemId = itemId) } }
            .launchIn(viewModelScope)
    }

    private fun loadProduct() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getProductDetailsUseCase(productId)) {
                is AppResult.Success -> {
                    val content = result.data
                    loadedProduct = content.details
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            product = content.details.toUi(),
                            reviews = content.reviews.map { review -> review.toUi() },
                            recommendedProducts = content.recommendedProducts.map { p -> p.toUi() }
                        )
                    }
                }

                is AppResult.Failure -> _uiState.update {
                    it.copy(isLoading = false, error = result.error.toUiText())
                }
            }
        }
    }

    private fun toggleFavorite() {
        val product = loadedProduct ?: return
        val shouldAdd = !_uiState.value.isFavorite
        viewModelScope.launch {
            if (shouldAdd) favoriteGateway.addFavorite(product)
            else favoriteGateway.removeFavorite(product.id)
            showSnackbar(
                messageRes = if (shouldAdd) R.string.product_added_to_favorite else R.string.product_removed_from_favorite,
                type = AlertType.Success
            )
        }
    }

    private fun onAddToCartActionClicked() {
        val current = _uiState.value
        if (current.product == null || current.isAddingToCart) return

        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToCart = true) }
            val cartItemId = current.cartItemId
            if (cartItemId != null) removeFromCart(cartItemId) else addToCart()
            _uiState.update { it.copy(isAddingToCart = false) }
        }
    }

    private suspend fun addToCart() {
        when (val result = addToCartUseCase(productId, DEFAULT_QUANTITY)) {
            is AppResult.Success -> {
                _uiState.update { it.copy(cartItemId = result.data ?: it.cartItemId) }
                refreshCart()
                showSnackbar(R.string.product_add_to_cart_successfully, AlertType.Success)
            }

            is AppResult.Failure -> showSnackbar(R.string.product_failed_to_add_to_cart, AlertType.Error)
        }
    }

    private suspend fun removeFromCart(cartItemId: String) {
        when (cartGateway.removeCartItem(cartItemId)) {
            is AppResult.Success -> {
                _uiState.update { it.copy(cartItemId = null) }
                refreshCart()
                showSnackbar(messageRes = R.string.product_remove_from_cart, type = AlertType.Success)
            }

            is AppResult.Failure -> showSnackbar(messageRes = R.string.product_failed_to_remove_item, type = AlertType.Error)
        }
    }

    private fun sendEffect(effect: ProductDetailsUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }

    private suspend fun showSnackbar(@StringRes messageRes: Int, type: AlertType) {
        _uiEffect.emit(ProductDetailsUiEffect.ShowSnackbar(message = UiText.Resource(messageRes), type))
    }

    companion object {
        const val PRODUCT_ID_ARG = "productId"
        private const val DEFAULT_QUANTITY = 1
    }
}