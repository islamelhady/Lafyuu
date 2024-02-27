package com.elhady.lafyuu.core.designsystem.component.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class LafyuuNavigationItem(
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon,
    @StringRes val labelRes: Int,
    val badgeCount: Int = 0,
)