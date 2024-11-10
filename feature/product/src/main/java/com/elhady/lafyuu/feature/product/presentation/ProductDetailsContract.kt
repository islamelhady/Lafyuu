package com.elhady.lafyuu.feature.product.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.product.presentation.model.ProductDetailsUi
import com.elhady.lafyuu.feature.product.presentation.model.RecommendedProductUi
import com.elhady.lafyuu.feature.product.presentation.model.ReviewUi

data class ProductDetailsUiState(
    val isLoading: Boolean = true,
    val isAddingToCart: Boolean = false,
    val cartItemId: String? = null,
    val product: ProductDetailsUi? = null,
    val reviews: List<ReviewUi> = emptyList(),
    val recommendedProducts: List<RecommendedProductUi> = emptyList(),
    val isFavorite: Boolean = false,
    val error: UiText? = null
) {
    val isInCart: Boolean get() = cartItemId != null
}

sealed interface ProductDetailsUiIntent {
    data object FavoriteToggled : ProductDetailsUiIntent
    data object AddToCartClicked : ProductDetailsUiIntent
    data object BackClicked : ProductDetailsUiIntent
    data object SearchClicked : ProductDetailsUiIntent
    data object RetryClicked : ProductDetailsUiIntent
    data object SeeMoreReviewsClicked : ProductDetailsUiIntent
    data object WriteReviewClicked : ProductDetailsUiIntent
    data class RecommendedProductClicked(val productId: String) : ProductDetailsUiIntent
}

sealed interface ProductDetailsUiEffect {
    data object NavigateBack : ProductDetailsUiEffect
    data class NavigateToProductDetails(val productId: String) : ProductDetailsUiEffect
    data class NavigateToReviews(val productId: String) : ProductDetailsUiEffect
    data class NavigateToWriteReview(val productId: String) : ProductDetailsUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : ProductDetailsUiEffect
    data object NavigateToSearch : ProductDetailsUiEffect
}
