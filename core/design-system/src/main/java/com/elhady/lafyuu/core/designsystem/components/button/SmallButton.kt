package com.elhady.lafyuu.core.designsystem.components.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme


@Composable
fun SmallButton(
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    BaseButton(
        caption = caption,
        onClick = onClick,
        modifier = modifier.width(99.dp),
        isEnabled = isEnabled
    )
}


@Preview(name = "Small Enable")
@Composable
private fun PreviewSmallWithoutIcon() {
    LafyuuTheme() {
        Box(modifier = Modifier.padding(Theme.space.large)) {
            SmallButton(
                caption = "Button",
                onClick = {},
                isEnabled = true
            )
        }
    }
}

@Preview(name = "Small Disable")
@Composable
private fun PreviewSmallWithoutIconDisable() {
    LafyuuTheme {
        Box(modifier = Modifier.padding(Theme.space.large)) {
            SmallButton(
                caption = "Button",
                onClick = {},
                isEnabled = false
            )
        }
    }
}