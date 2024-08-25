package com.elhady.lafyuu.feature.product.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.usecase.AddToCartUseCase
import com.elhady.lafyuu.feature.product.domain.usecase.GetProductDetailsUseCase
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
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
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
        }
    }

    private fun loadProductDetails(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
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
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Failed to load product details"
                        )
                    }
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
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
        val newFav = !_uiState.value.isFavorite
        _uiState.update { it.copy(isFavorite = newFav) }
        val msg = if (newFav) "Added to Wishlist" else "Removed from Wishlist"
        sendEffect(ProductDetailsUiEffect.ShowSnackbar(msg))
    }

    private fun addToCart() {
        val product = _uiState.value.product ?: return
        val qty = _uiState.value.quantity
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToCart = true) }
            when (val result = addToCartUseCase(product.id, qty)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isAddingToCart = false) }
                    sendEffect(ProductDetailsUiEffect.ShowSnackbar("Added to cart successfully!"))
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isAddingToCart = false) }
                    sendEffect(ProductDetailsUiEffect.ShowSnackbar(result.message ?: "Failed to add to cart"))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isAddingToCart = true) }
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
