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
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val titleArg: String = savedStateHandle["title"] ?: ""

    private val _uiState = MutableStateFlow(ProductListUiState(title = titleArg))
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ProductListUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        if (titleArg.isNotBlank()) {
            loadProducts(titleArg)
        }
    }

    fun onEvent(event: ProductListUiEvent) {
        when (event) {
            is ProductListUiEvent.LoadProducts -> loadProducts(event.title)
            is ProductListUiEvent.ProductClicked -> {
                sendEffect(ProductListUiEffect.NavigateToProductDetails(event.productId))
            }
            ProductListUiEvent.BackClicked -> sendEffect(ProductListUiEffect.NavigateBack)
            ProductListUiEvent.RetryClicked -> loadProducts(titleArg)
        }
    }

    private fun loadProducts(title: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, title = title) }
            val result = when (title) {
                "Flash Sale" -> getProductsUseCase(page = 1, pageSize = 20)
                "Mega Sale" -> getProductsUseCase(page = 2, pageSize = 20)
                else -> getProductsUseCase(category = title)
            }
            when (result) {
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
                    sendEffect(ProductListUiEffect.ShowSnackbar(msg, AlertType.Error))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun sendEffect(effect: ProductListUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
