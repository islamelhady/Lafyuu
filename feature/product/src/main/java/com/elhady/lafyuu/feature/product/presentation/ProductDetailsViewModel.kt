package com.elhady.lafyuu.feature.product.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.domain.usecase.GetProductDetailsUseCase
import com.elhady.lafyuu.core.database.repository.FavoritesRepository
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import com.elhady.lafyuu.feature.cart.domain.usecase.RemoveCartItemUseCase
import com.elhady.lafyuu.feature.product.R
import com.elhady.lafyuu.feature.product.domain.usecase.AddToCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val favoritesRepository: FavoritesRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ProductDetailsUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private val productIdFromNav: String? = savedStateHandle["productId"]

    init {
        productIdFromNav?.let { id ->
            if (id.isNotBlank()) {
                onEvent(ProductDetailsUiEvent.LoadProduct(id))
            }
        }
    }

    fun onEvent(event: ProductDetailsUiEvent) {
        when (event) {
            is ProductDetailsUiEvent.LoadProduct -> loadProductDetails(event.productId)
            is ProductDetailsUiEvent.SizeSelected -> selectSize(event.size)
            is ProductDetailsUiEvent.ColorSelected -> selectColor(event.colorHex)
            is ProductDetailsUiEvent.QuantityChanged -> updateQuantity(event.newQuantity)
            ProductDetailsUiEvent.FavoriteToggled -> toggleFavorite()
            ProductDetailsUiEvent.AddToCartClicked -> addToCart()
            ProductDetailsUiEvent.BackClicked -> sendEffect(ProductDetailsUiEffect.NavigateBack)
            ProductDetailsUiEvent.SearchClicked -> sendEffect(ProductDetailsUiEffect.NavigateToSearch)
            ProductDetailsUiEvent.RetryClicked -> {
                val currentId = _uiState.value.product?.id ?: productIdFromNav
                currentId?.let { loadProductDetails(it) }
            }

            is ProductDetailsUiEvent.RecommendedProductClicked -> sendEffect(
                ProductDetailsUiEffect.NavigateToProductDetails(event.productId)
            )

            ProductDetailsUiEvent.SeeMoreReviewsClicked -> {
                val productId = _uiState.value.product?.id ?: return
                sendEffect(ProductDetailsUiEffect.NavigateToReviews(productId))
            }

            ProductDetailsUiEvent.WriteReviewClicked -> {
                val productId = _uiState.value.product?.id ?: return
                sendEffect(ProductDetailsUiEffect.NavigateToWriteReview(productId))
            }
        }
    }

    private fun loadProductDetails(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            launch {
                favoritesRepository.isFavorite(productId).collectLatest { isFav ->
                    _uiState.update { it.copy(isFavorite = isFav) }
                }
            }

            launch {
                cartRepository.observeCart().collectLatest { cart ->
                    val cartItem = cart?.items?.find { it.productId == productId }
                    val inCart = cartItem != null
                    _uiState.update {
                        it.copy(
                            isInCart = inCart,
                            cartItemId = cartItem?.itemId
                        )
                    }
                }
            }

            when (val result = getProductDetailsUseCase(productId)) {
                is AppResult.Success -> {
                    val content = result.data
                    val defaultSize = content.details.availableSizes.firstOrNull().orEmpty()
                    val defaultColor = content.details.availableColorsHex.firstOrNull().orEmpty()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            product = content.details,
                            reviews = content.reviews,
                            recommendedProducts = content.recommendedProducts,
                            selectedSize = defaultSize,
                            selectedColorHex = defaultColor,
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

    private fun selectSize(size: String) {
        _uiState.update { it.copy(selectedSize = size) }
    }

    private fun selectColor(colorHex: String) {
        _uiState.update { it.copy(selectedColorHex = colorHex) }
    }

    private fun updateQuantity(newQuantity: Int) {
        if (newQuantity >= 1) {
            _uiState.update { it.copy(quantity = newQuantity) }
        }
    }

    private fun toggleFavorite() {
        val product = _uiState.value.product ?: return
        viewModelScope.launch {
            val currentFav = _uiState.value.isFavorite
            if (currentFav) {
                favoritesRepository.removeFavorite(product.id)
                sendEffect(
                    ProductDetailsUiEffect.ShowSnackbar(
                        message = UiText.Resource(R.string.removed_from_favorite),
                        type = AlertType.Success
                    )
                )
            } else {
                favoritesRepository.addFavorite(product.id)
                sendEffect(
                    ProductDetailsUiEffect.ShowSnackbar(
                        message = UiText.Resource(R.string.added_to_favorite),
                        type = AlertType.Success
                    )
                )
            }
        }
    }

    private fun addToCart() {
        val product = _uiState.value.product ?: return
        val isInCart = _uiState.value.isInCart
        val cartItemId = _uiState.value.cartItemId

        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToCart = true) }
            if (isInCart && cartItemId != null) {
                when (val result = removeCartItemUseCase(cartItemId)) {
                    is AppResult.Success -> {
                        _uiState.update { it.copy(isAddingToCart = false, isInCart = false) }
                        sendEffect(
                            ProductDetailsUiEffect.ShowSnackbar(
                                message = UiText.Resource(R.string.removed_from_cart),
                                type = AlertType.Success
                            )
                        )
                    }

                    is AppResult.Failure -> {
                        _uiState.update { it.copy(isAddingToCart = false) }
                        sendEffect(
                            ProductDetailsUiEffect.ShowSnackbar(
                                message = UiText.Resource(R.string.failed_to_remove_item),
                                type = AlertType.Error
                            )
                        )
                    }
                }
            } else {
                val qty = _uiState.value.quantity
                when (val result = addToCartUseCase(product.id, qty)) {
                    is AppResult.Success -> {
                        _uiState.update { it.copy(isAddingToCart = false, isInCart = true) }
                        sendEffect(
                            ProductDetailsUiEffect.ShowSnackbar(
                                message = UiText.Resource(R.string.added_to_cart_successfully),
                                type = AlertType.Success
                            )
                        )
                    }

                    is AppResult.Failure -> {
                        _uiState.update { it.copy(isAddingToCart = false) }
                        sendEffect(
                            ProductDetailsUiEffect.ShowSnackbar(
                                message = UiText.Resource(R.string.failed_to_add_to_cart),
                                type = AlertType.Error
                            )
                        )
                    }
                }
            }
        }
    }

    private fun sendEffect(effect: ProductDetailsUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
