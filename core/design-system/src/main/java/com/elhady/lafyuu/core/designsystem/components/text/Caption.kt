package com.elhady.lafyuu.core.designsystem.components.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun Caption(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.backgroundWhite,
    style: TextStyle = Theme.typography.largeCaptionBold
) {
    LafyuuText(
        text = text,
        modifier = modifier,
        style = style,
        color = color
    )
}
