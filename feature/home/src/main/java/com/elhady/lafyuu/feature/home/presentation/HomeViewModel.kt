package com.elhady.lafyuu.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.usecase.GetHomeContentUseCase
import com.elhady.lafyuu.feature.home.domain.usecase.SearchProductsUseCase
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
class HomeViewModel @Inject constructor(
    private val getHomeContentUseCase: GetHomeContentUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    private val _effect = Channel<HomeUiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadHomeData()
        observeSearchQuery()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.SearchQueryChanged -> onSearchQueryChange(event.query)
            is HomeUiEvent.ClearSearchClicked -> onClearSearch()
            is HomeUiEvent.CategoryClicked -> sendEffect(HomeUiEffect.NavigateToCategoryProducts(event.categoryId, event.categoryName))
            is HomeUiEvent.ProductClicked -> sendEffect(HomeUiEffect.NavigateToProductDetails(event.productId))
            is HomeUiEvent.FavoriteClicked -> onFavoriteClicked(event.productId)
            is HomeUiEvent.SeeMoreFlashSaleClicked -> sendEffect(HomeUiEffect.NavigateToFlashSale)
            is HomeUiEvent.SeeMoreMegaSaleClicked -> sendEffect(HomeUiEffect.NavigateToMegaSale)
            is HomeUiEvent.SeeMoreCategoryClicked -> sendEffect(HomeUiEffect.NavigateToCategoriesList)
            is HomeUiEvent.NotificationClicked -> sendEffect(HomeUiEffect.NavigateToNotifications)
            is HomeUiEvent.WishlistClicked -> sendEffect(HomeUiEffect.NavigateToWishlist)
            is HomeUiEvent.OfferBannerClicked -> sendEffect(HomeUiEffect.NavigateToProductDetails(event.offerId))
            is HomeUiEvent.BottomTabSelected -> onBottomTabSelected(event)
            is HomeUiEvent.RetryClicked -> loadHomeData()
        }
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getHomeContentUseCase()) {
                is AppResult.Success -> {
                    val content = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            categories = content.categories,
                            offers = content.offers,
                            flashSaleProducts = content.flashSaleProducts,
                            megaSaleProducts = content.megaSaleProducts,
                            recommendedProducts = content.recommendedProducts,
                            errorMessage = null
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                is AppResult.Loading -> {}
            }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _uiState.update {
                            it.copy(
                                searchQuery = "",
                                isSearching = false,
                                searchResults = emptyList()
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isSearching = true) }
                        when (val result = searchProductsUseCase(query)) {
                            is AppResult.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isSearching = false,
                                        searchResults = result.data
                                    )
                                }
                            }
                            is AppResult.Error -> {
                                _uiState.update {
                                    it.copy(
                                        isSearching = false,
                                        searchResults = emptyList()
                                    )
                                }
                            }
                            is AppResult.Loading -> {}
                        }
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        if (query.isBlank()) {
            _uiState.update {
                it.copy(
                    searchQuery = "",
                    isSearching = false,
                    searchResults = emptyList()
                )
            }
            _searchQuery.value = ""
        } else {
            _uiState.update { it.copy(searchQuery = query, isSearching = true) }
            _searchQuery.value = query
        }
    }

    fun onClearSearch() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                isSearching = false,
                searchResults = emptyList()
            )
        }
        _searchQuery.value = ""
    }

    private fun onFavoriteClicked(productId: String) {
        _uiState.update { currentState ->
            fun toggleFav(list: List<Product>) = list.map {
                if (it.id == productId) it.copy(isFavorite = !it.isFavorite) else it
            }
            currentState.copy(
                flashSaleProducts = toggleFav(currentState.flashSaleProducts),
                megaSaleProducts = toggleFav(currentState.megaSaleProducts),
                recommendedProducts = toggleFav(currentState.recommendedProducts),
                searchResults = toggleFav(currentState.searchResults)
            )
        }
    }

    private fun onBottomTabSelected(event: HomeUiEvent.BottomTabSelected) {
        _uiState.update { it.copy(selectedTab = event.tab.label) }
        sendEffect(HomeUiEffect.NavigateToTab(event.tab.route))
    }

    private fun sendEffect(effect: HomeUiEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
