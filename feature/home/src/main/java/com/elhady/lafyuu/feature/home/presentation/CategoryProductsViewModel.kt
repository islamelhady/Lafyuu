package com.elhady.lafyuu.feature.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.home.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryNameArg: String = savedStateHandle["categoryName"] ?: ""

    private val _uiState = MutableStateFlow(CategoryProductsUiState(categoryName = categoryNameArg))
    val uiState: StateFlow<CategoryProductsUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<CategoryProductsUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        if (categoryNameArg.isNotBlank()) {
            loadProducts(categoryNameArg)
        }
    }

    fun onEvent(event: CategoryProductsUiEvent) {
        when (event) {
            is CategoryProductsUiEvent.LoadCategoryProducts -> loadProducts(event.categoryName)
            is CategoryProductsUiEvent.ProductClicked -> {
                sendEffect(CategoryProductsUiEffect.NavigateToProductDetails(event.productId))
            }
            CategoryProductsUiEvent.BackClicked -> sendEffect(CategoryProductsUiEffect.NavigateBack)
            CategoryProductsUiEvent.RetryClicked -> loadProducts(categoryNameArg)
        }
    }

    private fun loadProducts(categoryName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, categoryName = categoryName) }
            when (val result = getProductsUseCase(category = categoryName)) {
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
                    val msg = result.message ?: "Failed to load products"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = msg
                        )
                    }
                    sendEffect(CategoryProductsUiEffect.ShowSnackbar(msg, AlertType.Error))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun sendEffect(effect: CategoryProductsUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
