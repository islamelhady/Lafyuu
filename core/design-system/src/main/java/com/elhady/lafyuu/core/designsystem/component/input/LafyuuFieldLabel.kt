package com.elhady.lafyuu.core.designsystem.component.input

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuColor
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuFieldLabel(
    text: String,
    modifier: Modifier = Modifier,
    required: Boolean = false
) {
    Text(
        text = if (required) "$text *" else text,
        modifier = modifier,
        style = LafyuuTypography.Label,
        color = LafyuuColor.TextPrimary
    )
}

@Preview
@Composable
fun LafyuuFieldLabelPreview() {
    LafyuuFieldLabel(
        text = "Field Label"
    )
}