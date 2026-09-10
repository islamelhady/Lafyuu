package com.elhady.lafyuu.core.designsystem.component.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    LafyuuText(
        text = text,
        modifier = modifier,
        style = LafyuuTypography.Title
    )
}

@Preview
@Composable
fun LafyuuTitlePreview() {
    LafyuuTitle(
        text = "Title"
    )
}