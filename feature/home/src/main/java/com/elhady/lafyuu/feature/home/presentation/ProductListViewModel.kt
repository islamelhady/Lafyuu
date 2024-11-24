package com.elhady.lafyuu.feature.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.home.R
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.model.SaleSection
import com.elhady.lafyuu.feature.home.domain.usecase.GetSaleProductsUseCase
import com.elhady.lafyuu.feature.home.navigation.PRODUCT_LIST_SECTION_ARG
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
class ProductListViewModel @Inject constructor(
    private val getSaleProducts: GetSaleProductsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val section = SaleSection.valueOf(
        checkNotNull(savedStateHandle.get<String>(PRODUCT_LIST_SECTION_ARG))
    )

    private val _uiState = MutableStateFlow(
        ProductListUiState(title = UiText.Resource(section.titleRes()))
    )
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ProductListUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<ProductListUiEffect> = _uiEffect.asSharedFlow()

    private var loadJob: Job? = null

    init {
        loadProducts()
    }

    fun onIntent(intent: ProductListUiIntent) {
        when (intent) {
            is ProductListUiIntent.ProductClicked ->
                sendEffect(ProductListUiEffect.NavigateToProductDetails(intent.productId))
            ProductListUiIntent.BackClicked -> sendEffect(ProductListUiEffect.NavigateBack)
            ProductListUiIntent.RetryClicked -> loadProducts()
        }
    }

    private fun loadProducts() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getSaleProducts(section)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, products = result.data.map(Product::toUi))
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(isLoading = false, error = result.error.toUiText())
                }
            }
        }
    }

    private fun sendEffect(effect: ProductListUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }
}

private fun SaleSection.titleRes(): Int = when (this) {
    SaleSection.Flash -> R.string.home_flash_sale
    SaleSection.Mega -> R.string.home_mega_sale
}