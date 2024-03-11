package com.elhady.lafyuu.core.designsystem.components.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun HintText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.neutralGrey
) {
    LafyuuText(
        text = text,
        modifier = modifier,
        style = Theme.typography.normalTextRegular,
        color = color
    )
}