package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.home.presentation.model.ProductUiModel

data class CategoryProductsUiState(
    val isLoading: Boolean = false,
    val categoryName: String = "",
    val products: List<ProductUiModel> = emptyList(),
    val error: UiText? = null
)

sealed interface CategoryProductsUiIntent {
    data class LoadCategoryProducts(val categoryName: String) : CategoryProductsUiIntent
    data class ProductClicked(val productId: String) : CategoryProductsUiIntent
    data object BackClicked : CategoryProductsUiIntent
    data object RetryClicked : CategoryProductsUiIntent
}

sealed interface CategoryProductsUiEffect {
    data object NavigateBack : CategoryProductsUiEffect
    data class NavigateToProductDetails(val productId: String) : CategoryProductsUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : CategoryProductsUiEffect
}
