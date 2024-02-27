package com.elhady.lafyuu.core.designsystem.component.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuColor
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuBodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LafyuuColor.TextSecondary
) {
    LafyuuText(
        text = text,
        modifier = modifier,
        style = LafyuuTypography.Body,
        color = color
    )
}

@Preview
@Composable
fun LafyuuBodyTextPreview() {
    LafyuuBodyText(
        text = "This is a preview of the LafyuuBodyText component."
    )
}