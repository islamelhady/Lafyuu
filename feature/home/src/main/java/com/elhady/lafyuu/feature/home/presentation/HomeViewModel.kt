package com.elhady.lafyuu.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.usecase.GetCategoriesUseCase
import com.elhady.lafyuu.feature.home.domain.usecase.GetOffersUseCase
import com.elhady.lafyuu.feature.home.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getOffersUseCase: GetOffersUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = Channel<HomeUiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadHomeData()
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

            val categoriesDeferred = async { getCategoriesUseCase() }
            val offersDeferred = async { getOffersUseCase() }
            val flashSaleDeferred = async { getProductsUseCase(page = 1, pageSize = 10) }
            val megaSaleDeferred = async { getProductsUseCase(page = 2, pageSize = 10) }
            val recommendedDeferred = async { getProductsUseCase(page = 1, pageSize = 20) }

            val categoriesRes = categoriesDeferred.await()
            val offersRes = offersDeferred.await()
            val flashSaleRes = flashSaleDeferred.await()
            val megaSaleRes = megaSaleDeferred.await()
            val recommendedRes = recommendedDeferred.await()

            val categories = (categoriesRes as? AppResult.Success)?.data ?: emptyList()
            val offers = (offersRes as? AppResult.Success)?.data ?: emptyList()

            val p1 = (flashSaleRes as? AppResult.Success)?.data ?: emptyList()
            val p2 = (megaSaleRes as? AppResult.Success)?.data ?: emptyList()
            val p3 = (recommendedRes as? AppResult.Success)?.data ?: emptyList()

            val allProducts = (p1 + p2 + p3).distinctBy { it.id }

            val flashSaleProducts = p1.ifEmpty { allProducts.take(6) }
            val megaSaleProducts = p2.ifEmpty { allProducts.take(6) }
            val recommendedProducts = p3.ifEmpty { allProducts }

            val hasError = (categoriesRes is AppResult.Error) &&
                    (flashSaleRes is AppResult.Error) &&
                    (recommendedRes is AppResult.Error)

            val errorMsg = if (hasError) {
                (categoriesRes as? AppResult.Error)?.message
                    ?: (flashSaleRes as? AppResult.Error)?.message
                    ?: (recommendedRes as? AppResult.Error)?.message
                    ?: (offersRes as? AppResult.Error)?.message
                    ?: "Failed to load content. Please check your network connection."
            } else null

            _uiState.update {
                it.copy(
                    isLoading = false,
                    categories = categories,
                    offers = offers,
                    flashSaleProducts = flashSaleProducts,
                    megaSaleProducts = megaSaleProducts,
                    recommendedProducts = recommendedProducts,
                    errorMessage = errorMsg
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        if (query.isBlank()) {
            _uiState.update { it.copy(isSearching = false, searchResults = emptyList()) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true) }
            when (val result = getProductsUseCase(searchTerm = query, pageSize = 20)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(searchResults = result.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(searchResults = emptyList()) }
                }
                is AppResult.Loading -> {}
            }
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
