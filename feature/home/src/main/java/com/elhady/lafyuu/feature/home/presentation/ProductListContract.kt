package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.home.presentation.model.ProductUiModel

data class ProductListUiState(
    val title: UiText,
    val isLoading: Boolean = false,
    val products: List<ProductUiModel> = emptyList(),
    val error: UiText? = null
)

sealed interface ProductListUiIntent {
    data class ProductClicked(val productId: String) : ProductListUiIntent
    data object BackClicked : ProductListUiIntent
    data object RetryClicked : ProductListUiIntent
}

sealed interface ProductListUiEffect {
    data object NavigateBack : ProductListUiEffect
    data class NavigateToProductDetails(val productId: String) : ProductListUiEffect
}
