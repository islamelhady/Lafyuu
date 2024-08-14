package com.elhady.lafyuu.feature.notifications.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val NOTIFICATIONS_ROUTE = "notifications"

fun NavController.navigateToNotifications(navOptions: NavOptions? = null) {
    this.navigate(NOTIFICATIONS_ROUTE, navOptions)
}

fun NavGraphBuilder.notificationsScreen(
    onNavigateBack: () -> Unit
) {
    composable(route = NOTIFICATIONS_ROUTE) {
        NotificationRoute(
            onNavigateBack = onNavigateBack
        )
    }
}
