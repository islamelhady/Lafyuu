package com.elhady.lafyuu.core.designsystem.component.foundation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.theme.LafyuuColor

@Composable
fun LafyuuLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = LafyuuColor.Primary,
        strokeWidth = 2.dp
    )
}

@Preview
@Composable
fun LafyuuLoadingIndicatorPreview() {
    LafyuuLoadingIndicator()
}