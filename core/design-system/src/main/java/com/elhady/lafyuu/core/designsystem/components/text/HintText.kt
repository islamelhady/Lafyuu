package com.elhady.lafyuu.core.designsystem.components.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun HintText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.neutralGrey,
    style: TextStyle = Theme.typography.normalTextRegular
) {
    LafyuuText(
        text = text,
        modifier = modifier,
        style = style,
        color = color
    )
}