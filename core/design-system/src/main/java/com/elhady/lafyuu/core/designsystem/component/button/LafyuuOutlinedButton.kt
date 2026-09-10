package com.elhady.lafyuu.core.designsystem.component.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuColor
import com.elhady.lafyuu.core.designsystem.theme.LafyuuDimens
import com.elhady.lafyuu.core.designsystem.theme.LafyuuShapes
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(LafyuuDimens.ButtonHeight),
        enabled = enabled,
        shape = LafyuuShapes.Medium,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = LafyuuColor.Primary
        )
    ) {
        Text(
            text = text,
            style = LafyuuTypography.LabelLarge
        )
    }
}

@Preview
@Composable
fun LafyuuOutlinedButtonPreview() {
    LafyuuOutlinedButton(
        text = "Button",
        onClick = {}
    )
}