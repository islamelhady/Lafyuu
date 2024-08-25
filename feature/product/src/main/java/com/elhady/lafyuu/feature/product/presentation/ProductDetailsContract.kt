package com.elhady.lafyuu.feature.product.presentation

import com.elhady.lafyuu.feature.product.domain.model.Product
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.model.ProductReview

data class ProductDetailsUiState(
    val isLoading: Boolean = false,
    val isAddingToCart: Boolean = false,
    val product: ProductDetails? = null,
    val reviews: List<ProductReview> = emptyList(),
    val recommendedProducts: List<Product> = emptyList(),
    val selectedSize: String = "",
    val selectedColorHex: String = "",
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val error: String? = null
)

sealed interface ProductDetailsUiEvent {
    data class LoadProduct(val productId: String) : ProductDetailsUiEvent
    data class SizeSelected(val size: String) : ProductDetailsUiEvent
    data class ColorSelected(val colorHex: String) : ProductDetailsUiEvent
    data class QuantityChanged(val newQuantity: Int) : ProductDetailsUiEvent
    data object FavoriteToggled : ProductDetailsUiEvent
    data object AddToCartClicked : ProductDetailsUiEvent
    data object BackClicked : ProductDetailsUiEvent
    data object SearchClicked : ProductDetailsUiEvent
    data object RetryClicked : ProductDetailsUiEvent
    data class RecommendedProductClicked(val productId: String) : ProductDetailsUiEvent
    data object SeeMoreReviewsClicked : ProductDetailsUiEvent
}

sealed interface ProductDetailsUiEffect {
    data object NavigateBack : ProductDetailsUiEffect
    data class NavigateToProductDetails(val productId: String) : ProductDetailsUiEffect
    data class NavigateToReviews(val productId: String) : ProductDetailsUiEffect
    data class ShowSnackbar(val message: String) : ProductDetailsUiEffect
    data object NavigateToSearch : ProductDetailsUiEffect
}
