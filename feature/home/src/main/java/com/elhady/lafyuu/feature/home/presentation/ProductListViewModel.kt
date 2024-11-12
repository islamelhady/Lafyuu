package com.elhady.lafyuu.feature.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.model.ProductQuery
import com.elhady.lafyuu.feature.home.domain.model.SaleSection
import com.elhady.lafyuu.feature.home.domain.usecase.GetProductsUseCase
import com.elhady.lafyuu.feature.home.domain.usecase.GetSaleProductsUseCase
import com.elhady.lafyuu.feature.home.presentation.mapper.toUiModel
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
    private val getSaleProductsUseCase: GetSaleProductsUseCase,
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

    fun onEvent(event: ProductListUiIntent) {
        when (event) {
            is ProductListUiIntent.LoadProducts -> loadProducts(event.title)
            is ProductListUiIntent.ProductClicked -> {
                sendEffect(ProductListUiEffect.NavigateToProductDetails(event.productId))
            }

            ProductListUiIntent.BackClicked -> sendEffect(ProductListUiEffect.NavigateBack)
            ProductListUiIntent.RetryClicked -> loadProducts(titleArg)
        }
    }

    private fun loadProducts(title: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, title = title) }
            val result = when (title) {
                "Flash Sale" -> getSaleProductsUseCase(section = SaleSection.Flash)
                "Mega Sale" -> getSaleProductsUseCase(section = SaleSection.Mega)
                else -> getProductsUseCase(query = ProductQuery(title))
            }
            when (result) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            products = result.data.map(Product::toUiModel),
                            error = null
                        )
                    }
                }

                is AppResult.Failure -> {
                    val errorMessage = result.error.toUiText()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                    sendEffect(ProductListUiEffect.ShowSnackbar(errorMessage, AlertType.Error))
                }
            }
        }
    }

    private fun sendEffect(effect: ProductListUiEffect) {
        viewModelScope.launch { _uiEffect.send(effect) }
    }
}
