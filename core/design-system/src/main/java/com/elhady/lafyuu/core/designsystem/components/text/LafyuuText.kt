package com.elhady.lafyuu.core.designsystem.components.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun LafyuuText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = Theme.typography.mediumTextRegular,
    softWrap: Boolean = true,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    color: Color = Theme.color.blue,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(color = color),
        onTextLayout = onTextLayout,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        overflow = overflow,
        textAlign = textAlign
    )
}
