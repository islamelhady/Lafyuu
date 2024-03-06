package com.elhady.lafyuu.core.designsystem.components.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    LafyuuText(
        text = text,
        modifier = modifier,
        style = Theme.typography.heading5
    )
}