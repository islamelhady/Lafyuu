package com.elhady.lafyuu.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elhady.lafyuu.feature.home.presentation.CategoriesRoute
import com.elhady.lafyuu.feature.home.presentation.CategoryProductsRoute
import com.elhady.lafyuu.feature.home.presentation.HomeRoute

const val HOME_ROUTE = "home"
const val CATEGORIES_ROUTE = "categories"
const val CATEGORY_PRODUCTS_ROUTE_BASE = "category_products"
const val CATEGORY_PRODUCTS_ROUTE = "$CATEGORY_PRODUCTS_ROUTE_BASE/{categoryName}"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HOME_ROUTE, navOptions)
}

fun NavController.navigateToCategories(navOptions: NavOptions? = null) {
    this.navigate(CATEGORIES_ROUTE, navOptions)
}

fun NavController.navigateToCategoryProducts(categoryName: String, navOptions: NavOptions? = null) {
    this.navigate("$CATEGORY_PRODUCTS_ROUTE_BASE/$categoryName", navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToSearch: () -> Unit = {},
    onNavigateToProductDetails: (String) -> Unit = {},
    onNavigateToFlashSale: () -> Unit = {},
    onNavigateToCategoryProducts: (String, String) -> Unit = { _, _ -> },
    onNavigateToMegaSale: () -> Unit = {},
    onNavigateToMoreCategories: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToWishlist: () -> Unit = {},
    onNavigateToTab: (String) -> Unit = {},
    cartItemCount: Int = 0
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToProductDetails = onNavigateToProductDetails,
            onNavigateToCategoryProducts = onNavigateToCategoryProducts,
            onNavigateToFlashSale = onNavigateToFlashSale,
            onNavigateToMegaSale = onNavigateToMegaSale,
            onNavigateToMoreCategories = onNavigateToMoreCategories,
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

fun NavGraphBuilder.categoryProductsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit
) {
    composable(
        route = CATEGORY_PRODUCTS_ROUTE,
        arguments = listOf(
            navArgument("categoryName") { type = NavType.StringType }
        )
    ) {
        CategoryProductsRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToProductDetails = onNavigateToProductDetails
        )
    }
}
