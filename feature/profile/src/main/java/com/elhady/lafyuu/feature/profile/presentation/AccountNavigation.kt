package com.elhady.lafyuu.feature.profile.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ACCOUNT_ROUTE = "account"
const val PROFILE_ROUTE = "profile"
const val CHANGE_PASSWORD_ROUTE = "change_password"

fun NavController.navigateToAccount(navOptions: NavOptions? = null) {
    this.navigate(ACCOUNT_ROUTE, navOptions)
}

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(PROFILE_ROUTE, navOptions)
}

fun NavController.navigateToChangePassword(navOptions: NavOptions? = null) {
    this.navigate(CHANGE_PASSWORD_ROUTE, navOptions)
}

fun NavGraphBuilder.accountScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAddress: () -> Unit,
    onNavigateToPayment: () -> Unit,
) {
    composable(route = ACCOUNT_ROUTE) {
        AccountRoute(
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToOrders = onNavigateToOrders,
            onNavigateToAddress = onNavigateToAddress,
            onNavigateToPayment = onNavigateToPayment
        )
    }
}

fun NavGraphBuilder.profileScreen(
    onNavigateToChangePassword: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit
) {
    composable(route = PROFILE_ROUTE) {
        ProfileRoute(
            onNavigateToChangePassword = onNavigateToChangePassword,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavGraphBuilder.changePasswordScreen(
    onNavigateBack: () -> Unit
) {
    composable(route = CHANGE_PASSWORD_ROUTE) {
        ChangePasswordRoute(
            onNavigateBack = onNavigateBack
        )
    }
}
