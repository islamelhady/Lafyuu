package com.elhady.lafyuu.feature.orders.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val ORDERS_ROUTE = "orders"
const val ORDER_DETAILS_ROUTE_BASE = "order_details"
const val ORDER_DETAILS_ROUTE = "$ORDER_DETAILS_ROUTE_BASE/{orderId}"

fun NavController.navigateToOrders(navOptions: NavOptions? = null) {
    this.navigate(ORDERS_ROUTE, navOptions)
}

fun NavController.navigateToOrderDetails(orderId: String, navOptions: NavOptions? = null) {
    this.navigate("$ORDER_DETAILS_ROUTE_BASE/$orderId", navOptions)
}

fun NavGraphBuilder.ordersScreen(
    onNavigateBack: () -> Unit,
    onNavigateToOrderDetails: (String) -> Unit
) {
    composable(route = ORDERS_ROUTE) {
        OrdersRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToOrderDetails = onNavigateToOrderDetails
        )
    }
}

fun NavGraphBuilder.orderDetailsScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = ORDER_DETAILS_ROUTE,
        arguments = listOf(
            navArgument("orderId") { type = NavType.StringType }
        )
    ) {
        OrderDetailsRoute(
            onNavigateBack = onNavigateBack
        )
    }
}
