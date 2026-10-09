package com.elhady.lafyuu.feature.explore.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem

data class ExploreUiState(
    val isLoading: Boolean = false,
    val groups: List<CategoryGroupUiModel> = emptyList(),
    val error: String? = null
)

sealed interface ExploreUiEvent {
    data object LoadCategories : ExploreUiEvent
    data class CategoryClicked(val categoryId: String, val categoryName: String) : ExploreUiEvent
    data object RetryClicked : ExploreUiEvent
    data object NotificationClicked : ExploreUiEvent
    data object WishlistClicked : ExploreUiEvent
    data object SearchClicked : ExploreUiEvent
    data class BottomTabSelected(val tab: TabBarItem) : ExploreUiEvent
}

sealed interface ExploreUiEffect {
    data class NavigateToCategoryProducts(val categoryId: String, val categoryName: String) : ExploreUiEffect
    data object NavigateToNotifications : ExploreUiEffect
    data object NavigateToWishlist : ExploreUiEffect
    data object NavigateToSearch : ExploreUiEffect
    data class NavigateToTab(val tabRoute: String) : ExploreUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : ExploreUiEffect
}
