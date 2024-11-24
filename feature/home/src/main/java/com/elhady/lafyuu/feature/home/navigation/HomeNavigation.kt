package com.elhady.lafyuu.feature.home.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elhady.lafyuu.feature.home.domain.model.SaleSection
import com.elhady.lafyuu.feature.home.presentation.CategoriesRoute
import com.elhady.lafyuu.feature.home.presentation.CategoryProductsRoute
import com.elhady.lafyuu.feature.home.presentation.HomeRoute
import com.elhady.lafyuu.feature.home.presentation.ProductListRoute

const val HOME_ROUTE = "home"
const val CATEGORIES_ROUTE = "categories"
const val CATEGORY_PRODUCTS_ROUTE_BASE = "category_products"
const val CATEGORY_NAME_ARG = "categoryName"
const val CATEGORY_PRODUCTS_ROUTE = "$CATEGORY_PRODUCTS_ROUTE_BASE/{$CATEGORY_NAME_ARG}"
const val PRODUCT_LIST_ROUTE_BASE = "product_list"
const val PRODUCT_LIST_SECTION_ARG = "section"
const val PRODUCT_LIST_ROUTE = "$PRODUCT_LIST_ROUTE_BASE/{$PRODUCT_LIST_SECTION_ARG}"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavController.navigateToCategories(navOptions: NavOptions? = null) {
    navigate(CATEGORIES_ROUTE, navOptions)
}

fun NavController.navigateToCategoryProducts(categoryName: String, navOptions: NavOptions? = null) {
    navigate("$CATEGORY_PRODUCTS_ROUTE_BASE/${Uri.encode(categoryName)}", navOptions)
}

fun NavController.navigateToFlashSale(navOptions: NavOptions? = null) {
    navigateToSaleList(SaleSection.Flash, navOptions)
}

fun NavController.navigateToMegaSale(navOptions: NavOptions? = null) {
    navigateToSaleList(SaleSection.Mega, navOptions)
}

private fun NavController.navigateToSaleList(section: SaleSection, navOptions: NavOptions?) {
    navigate("$PRODUCT_LIST_ROUTE_BASE/${section.name}", navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onNavigateToFlashSale: () -> Unit,
    onNavigateToCategoryProducts: (String, String) -> Unit,
    onNavigateToMegaSale: () -> Unit,
    onNavigateToMoreCategories: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToTab: (String) -> Unit,
    cartItemCount: Int,
    selectedTab: String
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
        arguments = listOf(navArgument(CATEGORY_NAME_ARG) { type = NavType.StringType })
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
        arguments = listOf(navArgument(PRODUCT_LIST_SECTION_ARG) { type = NavType.StringType })
    ) {
        ProductListRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToProductDetails = onNavigateToProductDetails
        )
    }
}