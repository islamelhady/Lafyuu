package com.elhady.lafyuu.core.designsystem.components.button

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.icons.Plus
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun IconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    borderColor: Color = Theme.color.neutralLight,
    modifier: Modifier = Modifier,
    shape: Shape = Theme.corner.small,
    loading: (@Composable () -> Unit)? = null,
){
    BaseButton(
        modifier = modifier.size(Theme.size.huge),
        onClick = onClick,
        icon = icon,
        loading = loading,
        hasBorder = true,
        borderColor = borderColor,
        containerColor = Color.Transparent,
        contentColor = Theme.color.neutralGrey,
        shape = shape
    )
}

@Preview(name = "Icon Button")
@Composable
private fun PreviewIconButton(){
    LafyuuTheme() {
        IconButton(
            icon = Plus,
            onClick = {},
        )
    }
}