package com.elhady.lafyuu.core.designsystem.lafyuu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object Theme {
    val color: LafyuuColor
        @Composable @ReadOnlyComposable get() = LocalColor.current

    val typography: LafyuuTypography
        @Composable @ReadOnlyComposable get() = LocalTypography.current

    val corner: Corner
        @Composable @ReadOnlyComposable get() = LocalCorner.current


    val space: Space
        @Composable @ReadOnlyComposable get() = LocalSpace.current

    val size: Size
        @Composable @ReadOnlyComposable get() = LocalSize.current
}