package com.elhady.lafyuu.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.elhady.lafyuu.feature.home.presentation.CategoriesRoute
import com.elhady.lafyuu.feature.home.presentation.HomeRoute

const val HOME_ROUTE = "home"
const val CATEGORIES_ROUTE = "categories"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HOME_ROUTE, navOptions)
}

fun NavController.navigateToCategories(navOptions: NavOptions? = null) {
    this.navigate(CATEGORIES_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToSearch: () -> Unit = {},
    onNavigateToProductDetails: (String) -> Unit = {},
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
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToProductDetails = onNavigateToProductDetails,
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

fun NavGraphBuilder.categoriesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCategoryProducts: (String, String) -> Unit
) {
    composable(route = CATEGORIES_ROUTE) {
        CategoriesRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToCategoryProducts = onNavigateToCategoryProducts
        )
    }
}
