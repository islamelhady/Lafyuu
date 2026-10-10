package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.home.domain.model.Product

data class CategoryProductsUiState(
    val isLoading: Boolean = false,
    val categoryName: String = "",
    val products: List<Product> = emptyList(),
    val error: String? = null
)

sealed interface CategoryProductsUiEvent {
    data class LoadCategoryProducts(val categoryName: String) : CategoryProductsUiEvent
    data class ProductClicked(val productId: String) : CategoryProductsUiEvent
    data object BackClicked : CategoryProductsUiEvent
    data object RetryClicked : CategoryProductsUiEvent
}

sealed interface CategoryProductsUiEffect {
    data object NavigateBack : CategoryProductsUiEffect
    data class NavigateToProductDetails(val productId: String) : CategoryProductsUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : CategoryProductsUiEffect
}
