package com.elhady.lafyuu.feature.home.presentation

import androidx.compose.runtime.Immutable
import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.home.domain.model.HomeSection
import com.elhady.lafyuu.feature.home.presentation.model.CategoryUiModel
import com.elhady.lafyuu.feature.home.presentation.model.OfferUiModel
import com.elhady.lafyuu.feature.home.presentation.model.ProductUiModel

@Immutable
data class HomeUiState(
    val isLoading: Boolean = true,
    val offers: List<OfferUiModel> = emptyList(),
    val categories: List<CategoryUiModel> = emptyList(),
    val flashSaleProducts: List<ProductUiModel> = emptyList(),
    val megaSaleProducts: List<ProductUiModel> = emptyList(),
    val recommendedProducts: List<ProductUiModel> = emptyList(),
    val failedSections: Set<HomeSection> = emptySet(),
    val error: UiText? = null
) {
    val isContentEmpty: Boolean
        get() = offers.isEmpty() &&
                categories.isEmpty() &&
                flashSaleProducts.isEmpty() &&
                megaSaleProducts.isEmpty() &&
                recommendedProducts.isEmpty()

    val hasLoadProblem: Boolean
        get() = error != null || failedSections.isNotEmpty()
}

sealed interface HomeUiIntent {
    data object SearchClicked : HomeUiIntent
    data class CategoryClicked(val categoryId: String, val categoryName: String) : HomeUiIntent
    data class ProductClicked(val productId: String) : HomeUiIntent
    data object SeeMoreFlashSaleClicked : HomeUiIntent
    data object SeeMoreMegaSaleClicked : HomeUiIntent
    data object SeeMoreCategoryClicked : HomeUiIntent
    data object NotificationClicked : HomeUiIntent
    data object FavouriteClicked : HomeUiIntent
    data class BottomTabSelected(val tab: TabBarItem) : HomeUiIntent
    data object RetryClicked : HomeUiIntent
}

sealed interface HomeUiEffect {
    data object NavigateToSearch : HomeUiEffect
    data class NavigateToProductDetails(val productId: String) : HomeUiEffect
    data class NavigateToCategoryProducts(val categoryId: String, val categoryName: String) : HomeUiEffect
    data object NavigateToFlashSale : HomeUiEffect
    data object NavigateToMegaSale : HomeUiEffect
    data object NavigateToMoreCategories : HomeUiEffect
    data object NavigateToNotifications : HomeUiEffect
    data object NavigateToFavourite : HomeUiEffect
    data class NavigateToTab(val tabRoute: String) : HomeUiEffect
}
