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
import com.elhady.lafyuu.feature.home.presentation.ProductListRoute

const val HOME_ROUTE = "home"
const val CATEGORIES_ROUTE = "categories"
const val CATEGORY_PRODUCTS_ROUTE_BASE = "category_products"
const val CATEGORY_PRODUCTS_ROUTE = "$CATEGORY_PRODUCTS_ROUTE_BASE/{categoryName}"
const val PRODUCT_LIST_ROUTE_BASE = "product_list"
const val PRODUCT_LIST_ROUTE = "$PRODUCT_LIST_ROUTE_BASE/{title}"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HOME_ROUTE, navOptions)
}

fun NavController.navigateToCategories(navOptions: NavOptions? = null) {
    this.navigate(CATEGORIES_ROUTE, navOptions)
}

fun NavController.navigateToCategoryProducts(categoryName: String, navOptions: NavOptions? = null) {
    this.navigate("$CATEGORY_PRODUCTS_ROUTE_BASE/$categoryName", navOptions)
}

fun NavController.navigateToProductList(title: String, navOptions: NavOptions? = null) {
    this.navigate("$PRODUCT_LIST_ROUTE_BASE/$title", navOptions)
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
    cartItemCount: Int = 0,
    selectedTab: String = "home"
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
            cartItemCount = cartItemCount,
            selectedTab = selectedTab
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

fun NavGraphBuilder.productListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit
) {
    composable(
        route = PRODUCT_LIST_ROUTE,
        arguments = listOf(
            navArgument("title") { type = NavType.StringType }
        )
    ) {
        ProductListRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToProductDetails = onNavigateToProductDetails
        )
    }
}
