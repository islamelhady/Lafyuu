package com.elhady.lafyuu.feature.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.elhady.lafyuu.feature.favorites.presentation.FavoritesRoute

const val FAVORITES_ROUTE = "favorites"

fun NavController.navigateToFavorites(navOptions: NavOptions? = null) {
    this.navigate(FAVORITES_ROUTE, navOptions)
}

fun NavGraphBuilder.favoritesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit
) {
    composable(route = FAVORITES_ROUTE) {
        FavoritesRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToProductDetails = onNavigateToProductDetails
        )
    }
}
