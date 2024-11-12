package com.elhady.lafyuu.feature.favorites.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.favorites.presentation.model.FavoriteProductUi

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val products: List<FavoriteProductUi> = emptyList()
) {
    val isEmpty: Boolean get() = !isLoading && products.isEmpty()
}

sealed interface FavoritesUiIntent {
    data object BackClicked : FavoritesUiIntent
    data class ProductClicked(val productId: String) : FavoritesUiIntent
    data class RemoveFavoriteClicked(val productId: String) : FavoritesUiIntent
}

sealed interface FavoritesUiEffect {
    data object NavigateBack : FavoritesUiEffect
    data class NavigateToProductDetails(val productId: String) : FavoritesUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : FavoritesUiEffect
}
