package com.elhady.lafyuu.core.designsystem.lafyuu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun LafyuuTheme(
    colorScheme: LafyuuColor = lightColorScheme,
    corner: Corner = defaultCorner,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColor provides colorScheme,
        LocalCorner provides corner,
        content = content
    )
}