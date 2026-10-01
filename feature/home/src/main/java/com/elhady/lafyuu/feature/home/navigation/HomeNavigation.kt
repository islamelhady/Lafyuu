package com.elhady.lafyuu.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.elhady.lafyuu.feature.home.presentation.HomeRoute

const val HOME_ROUTE = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToProductDetails: (String) -> Unit = {},
    onNavigateToCategory: (String, String) -> Unit = { _, _ -> },
    onNavigateToFlashSale: () -> Unit = {},
    onNavigateToMegaSale: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToWishlist: () -> Unit = {},
    onNavigateToTab: (String) -> Unit = {},
    cartItemCount: Int = 0
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            onNavigateToProductDetails = onNavigateToProductDetails,
            onNavigateToCategory = onNavigateToCategory,
            onNavigateToFlashSale = onNavigateToFlashSale,
            onNavigateToMegaSale = onNavigateToMegaSale,
            onNavigateToCategories = onNavigateToCategories,
            onNavigateToNotifications = onNavigateToNotifications,
            onNavigateToWishlist = onNavigateToWishlist,
            onNavigateToTab = onNavigateToTab,
            cartItemCount = cartItemCount
        )
    }
}
