package com.elhady.lafyuu.core.designsystem.lafyuu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun LafyuuTheme(
    colorScheme: LafyuuColor = lightColorScheme,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColor provides colorScheme,
        content = content
    )
}