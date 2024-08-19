package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem

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
    data object NavigateToCategoriesList : HomeUiEffect
    data object NavigateToNotifications : HomeUiEffect
    data object NavigateToWishlist : HomeUiEffect
    data class NavigateToTab(val tabRoute: String) : HomeUiEffect
    data class ShowSnackbar(val message: String) : HomeUiEffect
}
