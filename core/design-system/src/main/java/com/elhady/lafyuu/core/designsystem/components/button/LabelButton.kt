package com.elhady.lafyuu.core.designsystem.components.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun LabelButton(
    caption: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color = Color.Transparent,
    borderColor: Color = Theme.color.neutralLight,
    isLoading: Boolean = false,
    hasBorder: Boolean = false,
    contentColor: Color = Theme.color.blue,
    loading: (@Composable () -> Unit)? = null,
    isEnabled: Boolean = true,
    style: TextStyle = Theme.typography.normalCaptionRegular
) {
    BaseButton(
        modifier = modifier,
        onClick = onClick,
        loading = loading,
        caption = caption,
        isLoading = isLoading,
        isEnabled = isEnabled,
        hasBorder = hasBorder,
        borderColor = borderColor,
        containerColor = containerColor,
        contentColor = contentColor,
        style = style
    )
}



@Preview(
    name = "Secondary Label Button",
    showBackground = true
)
@Composable
private fun PreviewLabelLabelButton() {
    LafyuuTheme() {
        LabelButton(
            caption = "Label",
            modifier = Modifier
                .width(66.dp)
                .height(56.dp),
            onClick = {},
            containerColor = Theme.color.blue.copy(alpha = 0.1f),
            contentColor = Theme.color.blue,
        )
    }
}


@Preview(name = "Label Button")
@Composable
private fun PreviewNormalDisable() {
    LafyuuTheme() {
        LabelButton(
            modifier = Modifier
                .width(66.dp)
                .height(56.dp),
            caption = "Label",
            hasBorder = true,
            containerColor = Color.Transparent,
            contentColor = Theme.color.neutralGrey,
            borderColor = Theme.color.neutralLight,
            onClick = {},
            isEnabled = true
        )
    }
}


