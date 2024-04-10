package com.elhady.lafyuu.feature.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.elhady.lafyuu.feature.auth.presentation.email_verification.EmailVerificationScreen
import com.elhady.lafyuu.feature.auth.presentation.forgot_password.ForgotPasswordScreen
import com.elhady.lafyuu.feature.auth.presentation.login.LoginScreen
import com.elhady.lafyuu.feature.auth.presentation.otp.OtpScreen
import com.elhady.lafyuu.feature.auth.presentation.register.RegisterScreen
import com.elhady.lafyuu.feature.auth.presentation.reset_password.ResetPasswordScreen

const val AUTH_GRAPH_ROUTE = "auth_graph"
const val LOGIN_ROUTE = "login"
const val REGISTER_ROUTE = "register"
const val EMAIL_VERIFICATION_ROUTE = "email_verification/{email}"
const val FORGOT_PASSWORD_ROUTE = "forgot_password"
const val OTP_ROUTE = "otp/{email}"
const val RESET_PASSWORD_ROUTE = "reset_password/{email}/{otp}"

fun NavController.navigateToAuth(navOptions: NavOptions? = null) {
    this.navigate(AUTH_GRAPH_ROUTE, navOptions)
}

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    this.navigate(LOGIN_ROUTE, navOptions)
}

fun NavController.navigateToRegister(navOptions: NavOptions? = null) {
    this.navigate(REGISTER_ROUTE, navOptions)
}

fun NavController.navigateToEmailVerification(email: String, navOptions: NavOptions? = null) {
    this.navigate("email_verification/$email", navOptions)
}

fun NavController.navigateToForgotPassword(navOptions: NavOptions? = null) {
    this.navigate(FORGOT_PASSWORD_ROUTE, navOptions)
}

fun NavController.navigateToOtp(email: String, navOptions: NavOptions? = null) {
    this.navigate("otp/$email", navOptions)
}

fun NavController.navigateToResetPassword(email: String, otp: String, navOptions: NavOptions? = null) {
    this.navigate("reset_password/$email/$otp", navOptions)
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
            LoginScreen(
                onNavigateToHome = onNavigateToHome,
                onNavigateToRegister = { navController.navigateToRegister() },
                onNavigateToForgotPassword = { navController.navigateToForgotPassword() }
            )
        }
        composable(route = REGISTER_ROUTE) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToEmailVerification = { email -> navController.navigateToEmailVerification(email) }
            )
        }
        composable(route = EMAIL_VERIFICATION_ROUTE) {
            EmailVerificationScreen(
                onNavigateToLogin = {
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(AUTH_GRAPH_ROUTE) { inclusive = false }
                    }
                }
            )
        }
        composable(route = FORGOT_PASSWORD_ROUTE) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToOtp = { email -> navController.navigateToOtp(email) }
            )
        }
        composable(route = OTP_ROUTE) {
            OtpScreen(
                onNavigateToResetPassword = { email, otp -> navController.navigateToResetPassword(email, otp) },
                onNavigateToLogin = {
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(AUTH_GRAPH_ROUTE) { inclusive = false }
                    }
                }
            )
        }
        composable(route = RESET_PASSWORD_ROUTE) {
            ResetPasswordScreen(
                onNavigateToLogin = {
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(AUTH_GRAPH_ROUTE) { inclusive = false }
                    }
                }
            )
        }
    }
}
