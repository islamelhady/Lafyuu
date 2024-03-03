package com.elhady.lafyuu.core.designsystem.lafyuu

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

val defaultCorner: Corner = Corner(
    none = RectangleShape,
    small = RoundedCornerShape(5.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp),
    extraLarge = RoundedCornerShape(16.dp),
    fullRounded = RoundedCornerShape(100)
)