package com.elhady.lafyuu.feature.offers.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val OFFERS_ROUTE = "offer"

fun NavController.navigateToOffers(navOptions: NavOptions? = null) {
    this.navigate(OFFERS_ROUTE, navOptions)
}

fun NavGraphBuilder.offersScreen(
    onNavigateToTab: (String) -> Unit,
    selectedTab: String = "offer"
) {
    composable(route = OFFERS_ROUTE) {
        OffersRoute(
            onNavigateToTab = onNavigateToTab,
            selectedTab = selectedTab
        )
    }
}
