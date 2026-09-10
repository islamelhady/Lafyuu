package com.elhady.lafyuu.core.designsystem.component.foundation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LafyuuHorizontalSpacer(
    width: Dp,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier.width(width)
    )
}

@Composable
fun LafyuuVerticalSpacer(
    height: Dp,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier.height(height)
    )
}