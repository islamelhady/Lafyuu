package com.elhady.lafyuu.feature.search.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.search.domain.model.SearchFilters
import com.elhady.lafyuu.feature.search.domain.usecase.GetCategoriesUseCase
import com.elhady.lafyuu.feature.search.domain.usecase.SearchProductsUseCase
import com.elhady.lafyuu.feature.search.presentation.mapper.toDomain
import com.elhady.lafyuu.feature.search.presentation.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialQuery: String = savedStateHandle.get<String>(QUERY_ARG).orEmpty()

    private val _uiState = MutableStateFlow(SearchUiState(query = initialQuery))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SearchUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<SearchUiEffect> = _uiEffect.asSharedFlow()

    private val queryChanges = MutableStateFlow(initialQuery)
    private var searchJob: Job? = null
    private var loadMoreJob: Job? = null
    private var categoriesJob: Job? = null

    // Used to ignore a search that would repeat the one already done or running
    private var lastFilters: SearchFilters? = null
    private var currentPage = SearchProductsUseCase.FIRST_PAGE

    init {
        loadCategories()
        observeQuery()
        if (initialQuery.isNotBlank()) search()
    }

    fun onIntent(intent: SearchUiIntent) {
        when (intent) {
            is SearchUiIntent.QueryChanged -> {
                _uiState.update { it.copy(query = intent.query) }
                queryChanges.value = intent.query
            }

            SearchUiIntent.RetryClicked -> search(force = true)
            SearchUiIntent.LoadMore -> loadMore()

            SearchUiIntent.FilterClicked -> {
                _uiState.update { it.copy(sheet = SearchSheet.Filter, draft = it.filters) }
                if (_uiState.value.categories.isEmpty()) loadCategories()
            }

            SearchUiIntent.SortClicked -> _uiState.update { it.copy(sheet = SearchSheet.Sort) }
            SearchUiIntent.SheetDismissed -> _uiState.update { it.copy(sheet = SearchSheet.None) }

            is SearchUiIntent.CategorySelected ->
                _uiState.update { it.copy(draft = it.draft.copy(category = intent.category)) }

            is SearchUiIntent.PriceRangeChanged -> _uiState.update {
                it.copy(
                    draft = it.draft.copy(
                        isPriceFilterEnabled = true,
                        minPrice = intent.min.coerceIn(PRICE_RANGE_MIN, PRICE_RANGE_MAX),
                        maxPrice = intent.max.coerceIn(PRICE_RANGE_MIN, PRICE_RANGE_MAX)
                    )
                )
            }

            is SearchUiIntent.InStockChanged ->
                _uiState.update { it.copy(draft = it.draft.copy(inStockOnly = intent.inStockOnly)) }

            SearchUiIntent.ResetFilters -> _uiState.update { it.copy(draft = FilterDraft()) }
            SearchUiIntent.ApplyFilters -> applyFilters()

            is SearchUiIntent.SortSelected -> {
                _uiState.update {
                    it.copy(
                        sort = if (it.sort == intent.sort) null else intent.sort,
                        sheet = SearchSheet.None
                    )
                }
                search()
            }

            is SearchUiIntent.ProductClicked ->
                sendEffect(SearchUiEffect.NavigateToProductDetails(intent.productId))
            SearchUiIntent.BackToHomeClicked -> sendEffect(SearchUiEffect.NavigateToHome)
            SearchUiIntent.BackClicked -> sendEffect(SearchUiEffect.NavigateBack)
        }
    }

    private fun observeQuery() {
        queryChanges
            .debounce(QUERY_DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { search() }
            .launchIn(viewModelScope)
    }

    private fun applyFilters() {
        _uiState.update { state ->
            val draft = state.draft
            state.copy(
                // the user may type min > max, normalise it here
                filters = draft.copy(
                    minPrice = minOf(draft.minPrice, draft.maxPrice),
                    maxPrice = maxOf(draft.minPrice, draft.maxPrice)
                ),
                sheet = SearchSheet.None
            )
        }
        search()
    }

    private fun search(force: Boolean = false) {
        val state = _uiState.value
        val filters = state.filters.toDomain(state.query, state.sort)
        if (!force && filters == lastFilters) return

        lastFilters = filters
        searchJob?.cancel()
        loadMoreJob?.cancel()
        currentPage = SearchProductsUseCase.FIRST_PAGE

        if (filters.query.isEmpty()) {
            _uiState.update {
                it.copy(
                    searchedQuery = "",
                    isLoading = false,
                    isLoadingMore = false,
                    hasNextPage = false,
                    products = emptyList(),
                    error = null
                )
            }
            return
        }

        searchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    searchedQuery = filters.query,
                    isLoading = true,
                    isLoadingMore = false,
                    hasNextPage = false,
                    products = emptyList(),
                    error = null
                )
            }
            when (val result = searchProductsUseCase(filters, currentPage)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = result.data.items.map { product -> product.toUi() },
                        hasNextPage = result.data.hasNextPage
                    )
                }

                is AppResult.Failure -> _uiState.update {
                    it.copy(isLoading = false, error = result.error.toUiText())
                }
            }
        }
    }

    private fun loadMore() {
        val state = _uiState.value
        val filters = lastFilters ?: return
        if (state.isLoading || state.isLoadingMore || !state.hasNextPage) return

        loadMoreJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            when (val result = searchProductsUseCase(filters, currentPage + 1)) {
                is AppResult.Success -> {
                    currentPage += 1
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            // distinctBy: a shifted page must never produce duplicate grid keys
                            products = (it.products + result.data.items.map { product -> product.toUi() })
                                .distinctBy { product -> product.id },
                            hasNextPage = result.data.hasNextPage
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update { it.copy(isLoadingMore = false) }
                    _uiEffect.emit(
                        SearchUiEffect.ShowSnackbar(result.error.toUiText(), AlertType.Error)
                    )
                }
            }
        }
    }

    private fun loadCategories() {
        if (categoriesJob?.isActive == true) return
        categoriesJob = viewModelScope.launch {
            when (val result = getCategoriesUseCase()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(categories = result.data.map { category -> category.name })
                }

                is AppResult.Failure -> Unit // retried when the filter sheet is opened
            }
        }
    }

    private fun sendEffect(effect: SearchUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }

    companion object {
        const val QUERY_ARG = "query"
        private const val QUERY_DEBOUNCE_MS = 400L
    }
}
