package com.elhady.lafyuu.feature.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.elhady.lafyuu.feature.auth.presentation.change_password.ChangePasswordRoute
import com.elhady.lafyuu.feature.auth.presentation.forgot_password.ForgotPasswordRoute
import com.elhady.lafyuu.feature.auth.presentation.login.LoginRoute
import com.elhady.lafyuu.feature.auth.presentation.otp.OtpRoute
import com.elhady.lafyuu.feature.auth.presentation.register.RegisterRoute
import com.elhady.lafyuu.feature.auth.presentation.reset_password.ResetPasswordRoute

const val AUTH_GRAPH_ROUTE = "auth_graph"
const val LOGIN_ROUTE = "login"
const val REGISTER_ROUTE = "register"
const val OTP_ROUTE = "otp/{email}"
const val FORGOT_PASSWORD_ROUTE = "forgot_password"
const val RESET_PASSWORD_ROUTE = "reset_password/{email}"
const val CHANGE_PASSWORD_ROUTE = "change_password"

fun NavController.navigateToAuth(navOptions: NavOptions? = null) {
    this.navigate(AUTH_GRAPH_ROUTE, navOptions)
}

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    this.navigate(LOGIN_ROUTE, navOptions)
}

fun NavController.navigateToRegister(navOptions: NavOptions? = null) {
    this.navigate(REGISTER_ROUTE, navOptions)
}

fun NavController.navigateToOtp(email: String, navOptions: NavOptions? = null) {
    this.navigate("otp/$email", navOptions)
}

fun NavController.navigateToForgotPassword(navOptions: NavOptions? = null) {
    this.navigate(FORGOT_PASSWORD_ROUTE, navOptions)
}

fun NavController.navigateToResetPassword(email: String, navOptions: NavOptions? = null) {
    this.navigate("reset_password/$email", navOptions)
}

fun NavController.navigateToChangePassword(navOptions: NavOptions? = null) {
    this.navigate(CHANGE_PASSWORD_ROUTE, navOptions)
}

fun NavGraphBuilder.authGraph(
    onNavigateToHome: () -> Unit,
    navController: NavController
) {
    navigation(
        route = AUTH_GRAPH_ROUTE,
        startDestination = LOGIN_ROUTE
    ) {
        composable(route = LOGIN_ROUTE) {
            LoginRoute(
                onNavigateToHome = onNavigateToHome,
                onNavigateToRegister = { navController.navigateToRegister() },
                onNavigateToForgotPassword = { navController.navigateToForgotPassword() }
            )
        }
        composable(route = REGISTER_ROUTE) {
            RegisterRoute(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToOtp = { email -> navController.navigateToOtp(email) }
            )
        }
        composable(route = OTP_ROUTE) {
            OtpRoute(
                onNavigateToLogin = { 
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(AUTH_GRAPH_ROUTE) { inclusive = false }
                    }
                }
            )
        }
        composable(route = FORGOT_PASSWORD_ROUTE) {
            ForgotPasswordRoute(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResetPassword = { email -> navController.navigateToResetPassword(email) }
            )
        }
        composable(route = RESET_PASSWORD_ROUTE) {
            ResetPasswordRoute(
                onNavigateToLogin = {
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(AUTH_GRAPH_ROUTE) { inclusive = false }
                    }
                }
            )
        }
        composable(route = CHANGE_PASSWORD_ROUTE) {
            ChangePasswordRoute(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
