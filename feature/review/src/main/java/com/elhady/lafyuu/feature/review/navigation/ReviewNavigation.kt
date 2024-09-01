package com.elhady.lafyuu.feature.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elhady.lafyuu.feature.review.presentation.ReviewsRoute

const val REVIEWS_ARG = "productId"
const val REVIEWS_ROUTE_BASE = "reviews"
const val REVIEWS_ROUTE = "$REVIEWS_ROUTE_BASE/{$REVIEWS_ARG}?openWriteReview={openWriteReview}"

fun NavController.navigateToReviews(productId: String, openWriteReview: Boolean = false) {
    this.navigate("$REVIEWS_ROUTE_BASE/$productId?openWriteReview=$openWriteReview")
}

fun NavGraphBuilder.reviewsScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = REVIEWS_ROUTE,
        arguments = listOf(
            navArgument(REVIEWS_ARG) { type = NavType.StringType },
            navArgument("openWriteReview") {
                type = NavType.BoolType
                defaultValue = false
            }
        )
    ) {
        ReviewsRoute(
            onNavigateBack = onNavigateBack
        )
    }
}
