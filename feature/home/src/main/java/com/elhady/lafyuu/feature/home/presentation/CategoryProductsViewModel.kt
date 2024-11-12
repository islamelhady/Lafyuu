package com.elhady.lafyuu.feature.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.model.ProductQuery
import com.elhady.lafyuu.feature.home.domain.usecase.GetProductsUseCase
import com.elhady.lafyuu.feature.home.presentation.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _uiEffect = MutableSharedFlow<CategoryProductsUiEffect>(
        replay = 0, extraBufferCapacity = 64, onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        if (categoryNameArg.isNotBlank()) {
            loadProducts(categoryNameArg)
        }
    }

    fun onEvent(event: CategoryProductsUiIntent) {
        when (event) {
            is CategoryProductsUiIntent.LoadCategoryProducts -> loadProducts(event.categoryName)
            is CategoryProductsUiIntent.ProductClicked -> {
                sendEffect(CategoryProductsUiEffect.NavigateToProductDetails(event.productId))
            }
            CategoryProductsUiIntent.BackClicked -> sendEffect(CategoryProductsUiEffect.NavigateBack)
            CategoryProductsUiIntent.RetryClicked -> loadProducts(categoryNameArg)
        }
    }

    private fun loadProducts(categoryName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, categoryName = categoryName) }
            when (val result = getProductsUseCase(ProductQuery(category = categoryName))) {
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
                    sendEffect(CategoryProductsUiEffect.ShowSnackbar(errorMessage, type = AlertType.Error))
                }
            }
        }
    }

    private fun sendEffect(effect: CategoryProductsUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }
}
