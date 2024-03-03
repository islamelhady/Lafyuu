package com.elhady.lafyuu.core.designsystem.lafyuu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun LafyuuTheme(
    colorScheme: LafyuuColor = lightColorScheme,
    corner: Corner = defaultCorner,
    space: Space = defaultSpace,
    size: Size = defaultSize,
    typography: LafyuuTypography = defaultTypography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColor provides colorScheme,
        LocalCorner provides corner,
        LocalSpace provides space,
        LocalSize provides size,
        LocalTypography provides typography
    ){
        content()
    }
}