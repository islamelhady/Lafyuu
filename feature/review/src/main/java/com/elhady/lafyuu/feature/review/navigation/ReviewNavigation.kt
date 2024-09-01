package com.elhady.lafyuu.feature.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elhady.lafyuu.feature.review.presentation.ReviewsRoute
import com.elhady.lafyuu.feature.review.presentation.write.WriteReviewRoute

const val REVIEWS_ARG = "productId"
const val REVIEWS_ROUTE_BASE = "reviews"
const val REVIEWS_ROUTE = "$REVIEWS_ROUTE_BASE/{$REVIEWS_ARG}"

const val WRITE_REVIEW_ROUTE_BASE = "write_review"
const val WRITE_REVIEW_ROUTE = "$WRITE_REVIEW_ROUTE_BASE/{$REVIEWS_ARG}"

fun NavController.navigateToReviews(productId: String) {
    this.navigate("$REVIEWS_ROUTE_BASE/$productId")
}

fun NavController.navigateToWriteReview(productId: String) {
    this.navigate("$WRITE_REVIEW_ROUTE_BASE/$productId")
}

fun NavGraphBuilder.reviewsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToWriteReview: (String) -> Unit
) {
    composable(
        route = REVIEWS_ROUTE,
        arguments = listOf(
            navArgument(REVIEWS_ARG) { type = NavType.StringType }
        )
    ) {
        ReviewsRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToWriteReview = onNavigateToWriteReview
        )
    }
}

fun NavGraphBuilder.writeReviewScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = WRITE_REVIEW_ROUTE,
        arguments = listOf(
            navArgument(REVIEWS_ARG) { type = NavType.StringType }
        )
    ) {
        WriteReviewRoute(
            onNavigateBack = onNavigateBack
        )
    }
}
