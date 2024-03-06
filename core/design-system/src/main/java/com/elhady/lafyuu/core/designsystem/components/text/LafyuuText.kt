package com.elhady.lafyuu.core.designsystem.components.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun LafyuuText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = Theme.typography.mediumTextRegular,
    color: Color = Theme.color.blue,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines,
        overflow = overflow
    )
}
