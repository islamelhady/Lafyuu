package com.elhady.lafyuu.core.designsystem.components.button

import StarFilled
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun TextIconButton(
    icon: ImageVector,
    caption: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    loading: (@Composable () -> Unit)? = null,
) {
    BaseButton(
        modifier = modifier,
        onClick = onClick,
        caption = caption,
        icon = icon,
        iconTint = null,
        loading = loading,
        isLoading = isLoading,
        hasBorder = true,
        containerColor = Color.Transparent,
        disableContainerColor = Color.Transparent,
        contentColor = Theme.color.neutralGrey,
        disableContentColor = Theme.color.neutralGrey
    )
}

@Preview
@Composable
fun TextIconButtonPreview() {
    TextIconButton(
        onClick = {},
        caption = "Label",
        icon = StarFilled
    )
}