package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product

data class HomeUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val categories: List<Category> = emptyList(),
    val offers: List<Offer> = emptyList(),
    val flashSaleProducts: List<Product> = emptyList(),
    val megaSaleProducts: List<Product> = emptyList(),
    val recommendedProducts: List<Product> = emptyList(),
    val searchResults: List<Product> = emptyList(),
    val errorMessage: UiText? = null
) {
    val isContentEmpty: Boolean
        get() = !isLoading && categories.isEmpty() && flashSaleProducts.isEmpty() && megaSaleProducts.isEmpty() && recommendedProducts.isEmpty()
}

sealed interface HomeUiEvent {
    data class SearchQueryChanged(val query: String) : HomeUiEvent
    data object SearchClicked : HomeUiEvent
    data class CategoryClicked(val categoryId: String, val categoryName: String) : HomeUiEvent
    data class ProductClicked(val productId: String) : HomeUiEvent
    data class FavoriteClicked(val productId: String) : HomeUiEvent
    data object SeeMoreFlashSaleClicked : HomeUiEvent
    data object SeeMoreMegaSaleClicked : HomeUiEvent
    data object SeeMoreCategoryClicked : HomeUiEvent
    data object NotificationClicked : HomeUiEvent
    data object WishlistClicked : HomeUiEvent
    data class OfferBannerClicked(val offerId: String) : HomeUiEvent
    data class BottomTabSelected(val tab: TabBarItem) : HomeUiEvent
    data object RetryClicked : HomeUiEvent
}

sealed interface HomeUiEffect {
    data object NavigateToSearch : HomeUiEffect
    data class NavigateToProductDetails(val productId: String) : HomeUiEffect
    data class NavigateToCategoryProducts(val categoryId: String, val categoryName: String) : HomeUiEffect
    data object NavigateToFlashSale : HomeUiEffect
    data object NavigateToMegaSale : HomeUiEffect
    data object NavigateToMoreCategories : HomeUiEffect
    data object NavigateToNotifications : HomeUiEffect
    data object NavigateToWishlist : HomeUiEffect
    data class NavigateToTab(val tabRoute: String) : HomeUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : HomeUiEffect
}
