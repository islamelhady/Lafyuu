package com.elhady.lafyuu.feature.address.presentation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val ADDRESS_ROUTE = "address_route"
const val ADD_ADDRESS_ROUTE = "add_address_route"
const val EDIT_ADDRESS_ROUTE_BASE = "edit_address_route"
const val EDIT_ADDRESS_ROUTE = "$EDIT_ADDRESS_ROUTE_BASE/{${AddressFormViewModel.ADDRESS_ID_ARG}}"

fun NavController.navigateToAddress(navOptions: NavOptions? = null) {
    this.navigate(ADDRESS_ROUTE, navOptions)
}

fun NavController.navigateToAddAddress(navOptions: NavOptions? = null) {
    this.navigate(ADD_ADDRESS_ROUTE, navOptions)
}

fun NavController.navigateToEditAddress(addressId: String, navOptions: NavOptions? = null) {
    this.navigate("$EDIT_ADDRESS_ROUTE_BASE/${Uri.encode(addressId)}", navOptions)
}

fun NavGraphBuilder.addressScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddAddress: () -> Unit,
    onNavigateToEditAddress: (String) -> Unit
) {
    composable(ADDRESS_ROUTE) {
        AddressRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToAddAddress = onNavigateToAddAddress,
            onNavigateToEditAddress = onNavigateToEditAddress
        )
    }
}

fun NavGraphBuilder.addAddressScreen(
    onNavigateBack: () -> Unit
) {
    composable(ADD_ADDRESS_ROUTE) {
        AddressFormRoute(onNavigateBack = onNavigateBack)
    }
}

fun NavGraphBuilder.editAddressScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = EDIT_ADDRESS_ROUTE,
        arguments = listOf(
            navArgument(AddressFormViewModel.ADDRESS_ID_ARG) { type = NavType.StringType }
        )
    ) {
        AddressFormRoute(onNavigateBack = onNavigateBack)
    }
}
