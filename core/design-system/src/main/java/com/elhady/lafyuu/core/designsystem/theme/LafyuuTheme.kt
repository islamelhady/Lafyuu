package com.elhady.lafyuu.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.elhady.lafyuu.core.designsystem.lafyuu.Corner
import com.elhady.lafyuu.core.designsystem.lafyuu.LafyuuColor
import com.elhady.lafyuu.core.designsystem.lafyuu.LafyuuTypography
import com.elhady.lafyuu.core.designsystem.lafyuu.Size
import com.elhady.lafyuu.core.designsystem.lafyuu.Space
import com.elhady.lafyuu.core.designsystem.lafyuu.defaultCorner
import com.elhady.lafyuu.core.designsystem.lafyuu.defaultSize
import com.elhady.lafyuu.core.designsystem.lafyuu.defaultSpace
import com.elhady.lafyuu.core.designsystem.lafyuu.defaultTypography
import com.elhady.lafyuu.core.designsystem.lafyuu.lightColorScheme


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