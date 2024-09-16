package com.elhady.lafyuu.feature.favorites.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favoriteProducts: List<ProductDetails> = emptyList(),
    val error: String? = null
)

sealed interface FavoritesUiEvent {
    data object LoadFavorites : FavoritesUiEvent
    data class RemoveFavorite(val productId: String) : FavoritesUiEvent
    data class ProductClicked(val productId: String) : FavoritesUiEvent
    data object BackClicked : FavoritesUiEvent
}

sealed interface FavoritesUiEffect {
    data object NavigateBack : FavoritesUiEffect
    data class NavigateToProductDetails(val productId: String) : FavoritesUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : FavoritesUiEffect
}
