package com.elhady.lafyuu.feature.search.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.search.domain.model.Product

data class SearchUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val query: String = "",
    val category: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val isInStock: Boolean? = null,
    val sortBy: String? = null,
    val sortOrder: String? = null,
    val error: String? = null
)

sealed interface SearchUiEvent {
    data class QueryChanged(val query: String) : SearchUiEvent
    data class SearchSubmitted(val query: String) : SearchUiEvent
    data class CategoryFilterChanged(val category: String?) : SearchUiEvent
    data class PriceFilterChanged(val min: Double?, val max: Double?) : SearchUiEvent
    data class InStockFilterChanged(val isInStock: Boolean?) : SearchUiEvent
    data object SortClicked : SearchUiEvent
    data class ProductClicked(val productId: String) : SearchUiEvent
    data object BackClicked : SearchUiEvent
    data object RetryClicked : SearchUiEvent
}

sealed interface SearchUiEffect {
    data class NavigateToProductDetails(val productId: String) : SearchUiEffect
    data object NavigateBack : SearchUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : SearchUiEffect
}
