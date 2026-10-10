package com.elhady.lafyuu.feature.product.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsRoute
import com.elhady.lafyuu.feature.product.presentation.reviews.ReviewsRoute

const val PRODUCT_DETAILS_ARG = "productId"
const val PRODUCT_DETAILS_ROUTE_BASE = "product_details"
const val PRODUCT_DETAILS_ROUTE = "$PRODUCT_DETAILS_ROUTE_BASE/{$PRODUCT_DETAILS_ARG}"

const val REVIEWS_ROUTE_BASE = "reviews"
const val REVIEWS_ROUTE = "$REVIEWS_ROUTE_BASE/{$PRODUCT_DETAILS_ARG}"

fun NavController.navigateToProductDetails(productId: String) {
    this.navigate("$PRODUCT_DETAILS_ROUTE_BASE/$productId")
}

fun NavController.navigateToReviews(productId: String) {
    this.navigate("$REVIEWS_ROUTE_BASE/$productId")
}

fun NavGraphBuilder.reviewsScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = REVIEWS_ROUTE,
        arguments = listOf(
            navArgument(PRODUCT_DETAILS_ARG) { type = NavType.StringType }
        )
    ) {
        ReviewsRoute(
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavGraphBuilder.productDetailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onNavigateToReviews: (String) -> Unit
) {
    composable(
        route = PRODUCT_DETAILS_ROUTE,
        arguments = listOf(
            navArgument(PRODUCT_DETAILS_ARG) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val productId = backStackEntry.arguments?.getString(PRODUCT_DETAILS_ARG).orEmpty()
        ProductDetailsRoute(
            productId = productId,
            onNavigateBack = onNavigateBack,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToProductDetails = onNavigateToProductDetails,
            onNavigateToReviews = onNavigateToReviews
        )
    }
}
