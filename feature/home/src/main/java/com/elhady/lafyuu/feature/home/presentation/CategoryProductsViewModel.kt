package com.elhady.lafyuu.feature.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.model.ProductQuery
import com.elhady.lafyuu.feature.home.domain.usecase.GetProductsUseCase
import com.elhady.lafyuu.feature.home.navigation.CATEGORY_NAME_ARG
import com.elhady.lafyuu.feature.home.presentation.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryName: String =
        checkNotNull(savedStateHandle.get<String>(CATEGORY_NAME_ARG))

    private val _uiState = MutableStateFlow(CategoryProductsUiState(categoryName = categoryName))
    val uiState: StateFlow<CategoryProductsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CategoryProductsUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<CategoryProductsUiEffect> = _uiEffect.asSharedFlow()

    private var loadJob: Job? = null

    init {
        loadProducts()
    }

    fun onIntent(intent: CategoryProductsUiIntent) {
        when (intent) {
            is CategoryProductsUiIntent.ProductClicked ->
                sendEffect(CategoryProductsUiEffect.NavigateToProductDetails(intent.productId))
            CategoryProductsUiIntent.BackClicked -> sendEffect(CategoryProductsUiEffect.NavigateBack)
            CategoryProductsUiIntent.RetryClicked -> loadProducts()
        }
    }

    private fun loadProducts() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getProducts(ProductQuery(category = categoryName))) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, products = result.data.map(Product::toUi))
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(isLoading = false, error = result.error.toUiText())
                }
            }
        }
    }

    private fun sendEffect(effect: CategoryProductsUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }
}
