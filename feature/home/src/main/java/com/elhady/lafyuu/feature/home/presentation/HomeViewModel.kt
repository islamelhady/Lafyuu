package com.elhady.lafyuu.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.HomeContent
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.usecase.GetHomeContentUseCase
import com.elhady.lafyuu.feature.home.presentation.mapper.toUiModel
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

private const val MAX_OFFERS = 5
private const val HOME_PREVIEW_SIZE = 10

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeContent: GetHomeContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<HomeUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val effect: SharedFlow<HomeUiEffect> = _effect.asSharedFlow()

    private var loadJob: Job? = null

    init {
        loadHomeData()
    }

    fun onIntent(intent: HomeUiIntent) {
        when (intent) {
            HomeUiIntent.RetryClicked -> loadHomeData()
            HomeUiIntent.SearchClicked -> sendEffect(HomeUiEffect.NavigateToSearch)
            is HomeUiIntent.ProductClicked ->
                sendEffect(HomeUiEffect.NavigateToProductDetails(intent.productId))
            is HomeUiIntent.CategoryClicked ->
                sendEffect(HomeUiEffect.NavigateToCategoryProducts(intent.categoryId, intent.categoryName))
            HomeUiIntent.SeeMoreFlashSaleClicked -> sendEffect(HomeUiEffect.NavigateToFlashSale)
            HomeUiIntent.SeeMoreMegaSaleClicked -> sendEffect(HomeUiEffect.NavigateToMegaSale)
            HomeUiIntent.SeeMoreCategoryClicked -> sendEffect(HomeUiEffect.NavigateToMoreCategories)
            HomeUiIntent.NotificationClicked -> sendEffect(HomeUiEffect.NavigateToNotifications)
            HomeUiIntent.FavouriteClicked -> sendEffect(HomeUiEffect.NavigateToFavourite)
            is HomeUiIntent.BottomTabSelected -> sendEffect(HomeUiEffect.NavigateToTab(intent.tab.route))
        }
    }

    private fun loadHomeData() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getHomeContent()) {
                is AppResult.Success -> _uiState.update { it.withContent(result.data) }
                is AppResult.Failure -> _uiState.update {
                    it.copy(isLoading = false, error = result.error.toUiText())
                }
            }
        }
    }

    private fun sendEffect(effect: HomeUiEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}

private fun HomeUiState.withContent(content: HomeContent) = copy(
    isLoading = false,
    error = null,
    offers = content.offers.take(MAX_OFFERS).map(Offer::toUiModel),
    categories = content.categories.map(Category::toUiModel),
    flashSaleProducts = content.flashSaleProducts.take(HOME_PREVIEW_SIZE).map(Product::toUiModel),
    megaSaleProducts = content.megaSaleProducts.take(HOME_PREVIEW_SIZE).map(Product::toUiModel),
    recommendedProducts = content.recommendedProducts.map(Product::toUiModel),
    failedSections = content.failedSections
)