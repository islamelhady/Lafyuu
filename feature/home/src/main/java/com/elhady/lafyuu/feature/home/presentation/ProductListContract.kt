package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.home.domain.model.Product

data class ProductListUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    val products: List<Product> = emptyList(),
    val error: String? = null
)

sealed interface ProductListUiEvent {
    data class LoadProducts(val title: String) : ProductListUiEvent
    data class ProductClicked(val productId: String) : ProductListUiEvent
    data object BackClicked : ProductListUiEvent
    data object RetryClicked : ProductListUiEvent
}

sealed interface ProductListUiEffect {
    data object NavigateBack : ProductListUiEffect
    data class NavigateToProductDetails(val productId: String) : ProductListUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : ProductListUiEffect
}
