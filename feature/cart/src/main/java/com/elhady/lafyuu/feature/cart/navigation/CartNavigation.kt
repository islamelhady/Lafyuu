package com.elhady.lafyuu.feature.cart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.elhady.lafyuu.feature.cart.presentation.CartRoute

const val CART_ROUTE = "cart"

fun NavController.navigateToCart(navOptions: NavOptions? = null) {
    this.navigate(CART_ROUTE, navOptions)
}

fun NavGraphBuilder.cartScreen(
    onNavigateToCheckout: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onNavigateToTab: (String) -> Unit = {}
) {
    composable(route = CART_ROUTE) {
        CartRoute(
            onNavigateToCheckout = onNavigateToCheckout,
            onNavigateToProductDetails = onNavigateToProductDetails,
            onNavigateToTab = onNavigateToTab
        )
    }
}
