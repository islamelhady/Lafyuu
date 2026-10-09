package com.elhady.lafyuu.feature.explore.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val EXPLORE_ROUTE = "explore"

fun NavController.navigateToExplore(navOptions: NavOptions? = null) {
    this.navigate(EXPLORE_ROUTE, navOptions)
}

fun NavGraphBuilder.exploreScreen(
    onNavigateToCategoryProducts: (String, String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToTab: (String) -> Unit
) {
    composable(route = EXPLORE_ROUTE) {
        ExploreRoute(
            onNavigateToCategoryProducts = onNavigateToCategoryProducts,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToNotifications = onNavigateToNotifications,
            onNavigateToWishlist = onNavigateToWishlist,
            onNavigateToTab = onNavigateToTab
        )
    }
}
