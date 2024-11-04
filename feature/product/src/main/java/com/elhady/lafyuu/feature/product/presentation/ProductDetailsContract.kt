package com.elhady.lafyuu.feature.product.presentation

import com.elhady.lafyuu.core.common.domain.model.Product
import com.elhady.lafyuu.core.common.domain.model.ProductDetails
import com.elhady.lafyuu.core.common.domain.model.ProductReview
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText

data class ProductDetailsUiState(
    val isLoading: Boolean = false,
    val isAddingToCart: Boolean = false,
    val isInCart: Boolean = false,
    val cartItemId: String? = null,
    val product: ProductDetails? = null,
    val reviews: List<ProductReview> = emptyList(),
    val recommendedProducts: List<Product> = emptyList(),
    val selectedSize: String = "",
    val selectedColorHex: String = "",
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val error: UiText? = null
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
    data object WriteReviewClicked : ProductDetailsUiEvent
}

sealed interface ProductDetailsUiEffect {
    data object NavigateBack : ProductDetailsUiEffect
    data class NavigateToProductDetails(val productId: String) : ProductDetailsUiEffect
    data class NavigateToReviews(val productId: String) : ProductDetailsUiEffect
    data class NavigateToWriteReview(val productId: String) : ProductDetailsUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : ProductDetailsUiEffect
    data object NavigateToSearch : ProductDetailsUiEffect
}
