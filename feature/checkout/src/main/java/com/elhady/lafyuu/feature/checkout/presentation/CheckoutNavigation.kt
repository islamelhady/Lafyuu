package com.elhady.lafyuu.feature.checkout.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val CHECKOUT_ROUTE = "checkout"
const val ADD_ADDRESS_ROUTE = "add_address"
const val EDIT_ADDRESS_ROUTE_BASE = "edit_address"
const val EDIT_ADDRESS_ROUTE = "$EDIT_ADDRESS_ROUTE_BASE/{addressId}"
const val PAYMENT_ROUTE_BASE = "payment"
const val PAYMENT_ROUTE = "$PAYMENT_ROUTE_BASE/{shippingAddressId}/{paymentMethod}"

fun NavController.navigateToCheckout(navOptions: NavOptions? = null) {
    this.navigate(CHECKOUT_ROUTE, navOptions)
}

fun NavController.navigateToAddAddress(navOptions: NavOptions? = null) {
    this.navigate(ADD_ADDRESS_ROUTE, navOptions)
}

fun NavController.navigateToEditAddress(addressId: String, navOptions: NavOptions? = null) {
    this.navigate("$EDIT_ADDRESS_ROUTE_BASE/$addressId", navOptions)
}

fun NavController.navigateToPayment(shippingAddressId: String, paymentMethod: String, navOptions: NavOptions? = null) {
    this.navigate("$PAYMENT_ROUTE_BASE/$shippingAddressId/$paymentMethod", navOptions)
}

fun NavGraphBuilder.checkoutScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPayment: (String, String) -> Unit,
    onNavigateToAddAddress: () -> Unit,
    onNavigateToEditAddress: (String) -> Unit
) {
    composable(route = CHECKOUT_ROUTE) {
        CheckoutRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToPayment = onNavigateToPayment,
            onNavigateToAddAddress = onNavigateToAddAddress,
            onNavigateToEditAddress = onNavigateToEditAddress
        )
    }
}

fun NavGraphBuilder.addAddressScreen(
    onNavigateBack: () -> Unit
) {
    composable(route = ADD_ADDRESS_ROUTE) {
        AddAddressRoute(
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavGraphBuilder.editAddressScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = EDIT_ADDRESS_ROUTE,
        arguments = listOf(
            navArgument("addressId") { type = NavType.StringType }
        )
    ) {
        EditAddressRoute(
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavGraphBuilder.paymentScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSuccess: () -> Unit,
    onOpenUrl: (String) -> Unit
) {
    composable(
        route = PAYMENT_ROUTE,
        arguments = listOf(
            navArgument("shippingAddressId") { type = NavType.StringType },
            navArgument("paymentMethod") { type = NavType.StringType }
        )
    ) {
        PaymentRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToSuccess = onNavigateToSuccess,
            onOpenUrl = onOpenUrl
        )
    }
}
