package com.elhady.lafyuu.feature.search.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val SEARCH_ROUTE_BASE = "search"
const val SEARCH_ROUTE = "$SEARCH_ROUTE_BASE?query={query}"

fun NavController.navigateToSearch(query: String? = null, navOptions: NavOptions? = null) {
    val route = if (!query.isNullOrBlank()) "$SEARCH_ROUTE_BASE?query=$query" else SEARCH_ROUTE_BASE
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.searchScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit
) {
    composable(
        route = SEARCH_ROUTE,
        arguments = listOf(
            navArgument("query") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        SearchRoute(
            onNavigateToHome = onNavigateToHome,
            onNavigateToProductDetails = onNavigateToProductDetails
        )
    }
}
