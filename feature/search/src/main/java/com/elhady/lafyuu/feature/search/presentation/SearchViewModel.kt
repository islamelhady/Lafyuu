package com.elhady.lafyuu.feature.search.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.search.domain.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialQuery: String = savedStateHandle["query"] ?: ""

    private val _uiState = MutableStateFlow(SearchUiState(query = initialQuery))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow(initialQuery)

    private val _uiEffect = Channel<SearchUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        observeSearchQuery()
        searchProducts()
    }

    fun onEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.QueryChanged -> {
                _uiState.update { it.copy(query = event.query) }
                _searchQuery.value = event.query
            }
            is SearchUiEvent.SearchSubmitted -> {
                _uiState.update { it.copy(query = event.query) }
                searchProducts()
            }
            is SearchUiEvent.CategoryFilterChanged -> {
                _uiState.update { it.copy(category = event.category) }
                searchProducts()
            }
            is SearchUiEvent.PriceFilterChanged -> {
                _uiState.update { it.copy(minPrice = event.min, maxPrice = event.max) }
                searchProducts()
            }
            is SearchUiEvent.InStockFilterChanged -> {
                _uiState.update { it.copy(isInStock = event.isInStock) }
                searchProducts()
            }
            is SearchUiEvent.ProductClicked -> {
                sendEffect(SearchUiEffect.NavigateToProductDetails(event.productId))
            }
            SearchUiEvent.BackClicked -> {
                sendEffect(SearchUiEffect.NavigateBack)
            }
            SearchUiEvent.RetryClicked -> {
                searchProducts()
            }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(400L)
                .distinctUntilChanged()
                .collectLatest {
                    searchProducts()
                }
        }
    }

    private fun searchProducts() {
        viewModelScope.launch {
            val state = _uiState.value
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = searchProductsUseCase(
                searchTerm = state.query.ifBlank { null },
                category = state.category,
                minPrice = state.minPrice,
                maxPrice = state.maxPrice,
                isInStock = state.isInStock
            )) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            products = result.data,
                            error = null
                        )
                    }
                }
                is AppResult.Error -> {
                    val message = result.message ?: "Failed to search products"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = message
                        )
                    }
                    sendEffect(SearchUiEffect.ShowSnackbar(message, AlertType.Error))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun sendEffect(effect: SearchUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
