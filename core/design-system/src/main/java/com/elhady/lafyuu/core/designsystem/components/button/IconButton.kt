package com.elhady.lafyuu.core.designsystem.components.button

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.icons.Plus
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun IconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    iconSize: Dp = Theme.size.medium,
    borderColor: Color = Theme.color.neutralLight,
    modifier: Modifier = Modifier,
    shape: Shape = Theme.corner.small,
    loading: (@Composable () -> Unit)? = null,
) {
    LafyuuButton(
        modifier = modifier.size(Theme.size.huge),
        onClick = onClick,
        icon = icon,
        iconSize = iconSize,
        paddingHorizontal = 0.dp,
        loading = loading,
        hasBorder = true,
        borderColor = borderColor,
        containerColor = Theme.color.backgroundWhite,
        contentColor = Theme.color.neutralGrey,
        shape = shape
    )
}

@Preview(name = "Icon Button", showBackground = true)
@Composable
private fun PreviewIconButton() {
    LafyuuTheme() {
        IconButton(
            icon = Plus,
            onClick = {},
        )
    }
}