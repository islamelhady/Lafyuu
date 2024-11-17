package com.elhady.lafyuu.feature.search.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.search.domain.model.ProductSort
import com.elhady.lafyuu.feature.search.presentation.model.ProductUi

// Assumption: no max price comes from the API, 20000 is the slider upper bound
const val PRICE_RANGE_MIN = 0
const val PRICE_RANGE_MAX = 20000

data class FilterDraft(
    val category: String? = null,
    val isPriceFilterEnabled: Boolean = false,
    val minPrice: Int = PRICE_RANGE_MIN,
    val maxPrice: Int = PRICE_RANGE_MAX,
    val inStockOnly: Boolean = false
)

enum class SearchSheet { None, Filter, Sort }

data class SearchUiState(
    val query: String = "",
    // query of the last started search, used to tell "searching" from "not found"
    val searchedQuery: String = "",
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasNextPage: Boolean = false,
    val products: List<ProductUi> = emptyList(),
    val error: UiText? = null,
    val categories: List<String> = emptyList(),
    val filters: FilterDraft = FilterDraft(),
    val draft: FilterDraft = FilterDraft(),
    val sort: ProductSort? = null,
    val sheet: SearchSheet = SearchSheet.None
) {
    val isSearchPending: Boolean get() = isLoading || searchedQuery != query.trim()
}

sealed interface SearchUiIntent {
    data class QueryChanged(val query: String) : SearchUiIntent
    data object RetryClicked : SearchUiIntent
    data object LoadMore : SearchUiIntent
    data object FilterClicked : SearchUiIntent
    data object SortClicked : SearchUiIntent
    data object SheetDismissed : SearchUiIntent
    data class CategorySelected(val category: String?) : SearchUiIntent
    data class PriceRangeChanged(val min: Int, val max: Int) : SearchUiIntent
    data class InStockChanged(val inStockOnly: Boolean) : SearchUiIntent
    data object ResetFilters : SearchUiIntent
    data object ApplyFilters : SearchUiIntent
    data class SortSelected(val sort: ProductSort) : SearchUiIntent
    data class ProductClicked(val productId: String) : SearchUiIntent
    data object BackToHomeClicked : SearchUiIntent
    data object BackClicked : SearchUiIntent
}

sealed interface SearchUiEffect {
    data class NavigateToProductDetails(val productId: String) : SearchUiEffect
    data object NavigateToHome : SearchUiEffect
    data object NavigateBack : SearchUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : SearchUiEffect
}
