package com.elhady.lafyuu.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = LafyuuColor.Primary,
    onPrimary = LafyuuColor.TextOnPrimary,
    primaryContainer = LafyuuColor.SelectionBlue,
    onPrimaryContainer = LafyuuColor.Primary,

    secondary = LafyuuColor.AccentPink,
    onSecondary = LafyuuColor.TextOnPrimary,
    secondaryContainer = LafyuuColor.OfferPink,
    onSecondaryContainer = LafyuuColor.AccentPink,

    background = LafyuuColor.Background,
    onBackground = LafyuuColor.TextPrimary,

    surface = LafyuuColor.Background,
    onSurface = LafyuuColor.TextPrimary,

    surfaceVariant = LafyuuColor.Surface,
    onSurfaceVariant = LafyuuColor.TextSecondary,

    error = LafyuuColor.Error,
    onError = LafyuuColor.TextOnPrimary,

    outline = LafyuuColor.Border,
    outlineVariant = LafyuuColor.Divider
)

private val DarkColorScheme = darkColorScheme(
    primary = LafyuuColor.Primary,
    onPrimary = LafyuuColor.TextOnPrimary,

    primaryContainer = LafyuuColor.PrimaryContainer,
    onPrimaryContainer = LafyuuColor.OnPrimaryContainer,

    secondary = LafyuuColor.AccentPink,
    onSecondary = LafyuuColor.TextOnPrimary,

    secondaryContainer = LafyuuColor.SecondaryContainer,
    onSecondaryContainer = LafyuuColor.OnSecondaryContainer,

    background = LafyuuColor.Background,
    onBackground = LafyuuColor.OnBackground,

    surface = LafyuuColor.Surface,
    onSurface = LafyuuColor.OnSurface,

    surfaceVariant = LafyuuColor.SurfaceVariant,
    onSurfaceVariant = LafyuuColor.OnSurfaceVariant,

    error = LafyuuColor.Error,
    onError = LafyuuColor.Error,

    outline = LafyuuColor.Outline,
    outlineVariant = LafyuuColor.OutlineVariant,
)

@Composable
fun LafyuuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LafyuuTypography.Material,
        shapes = LafyuuShapes.Material,
        content = content
    )
}
