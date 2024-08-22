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
    val isFilterSheetOpen: Boolean = false,
    val tempMinPrice: Double? = null,
    val tempMaxPrice: Double? = null,
    val tempIsInStock: Boolean? = null,
    val error: String? = null
)

sealed interface SearchUiEvent {
    data class QueryChanged(val query: String) : SearchUiEvent
    data class SearchSubmitted(val query: String) : SearchUiEvent
    data class CategoryFilterChanged(val category: String?) : SearchUiEvent
    data class PriceRangeChanged(val min: Double?, val max: Double?) : SearchUiEvent
    data class InStockChanged(val isInStock: Boolean?) : SearchUiEvent
    data object FilterClicked : SearchUiEvent
    data object FilterDismissed : SearchUiEvent
    data object ApplyFilters : SearchUiEvent
    data object ResetFilters : SearchUiEvent
    data object SortClicked : SearchUiEvent
    data class ProductClicked(val productId: String) : SearchUiEvent
    data object BackClicked : SearchUiEvent
    data object BackToHomeClick : SearchUiEvent
}

sealed interface SearchUiEffect {
    data class NavigateToProductDetails(val productId: String) : SearchUiEffect
    data object NavigateToHome : SearchUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : SearchUiEffect
}
