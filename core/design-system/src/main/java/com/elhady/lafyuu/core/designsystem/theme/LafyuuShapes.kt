package com.elhady.lafyuu.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object LafyuuShapes {

    val Small = RoundedCornerShape(4.dp)

    val Medium = RoundedCornerShape(8.dp)

    val Large = RoundedCornerShape(12.dp)

    val ExtraLarge = RoundedCornerShape(16.dp)

    val Pill = RoundedCornerShape(50)

    val None = RoundedCornerShape(0.dp)

    val Material = Shapes(
        small = Small,
        medium = Medium,
        large = Large,
        extraLarge = ExtraLarge
    )
}