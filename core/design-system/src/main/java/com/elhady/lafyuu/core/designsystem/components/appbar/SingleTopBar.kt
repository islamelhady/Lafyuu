package com.elhady.lafyuu.core.designsystem.components.appbar

import Left
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SingleTopAppBar(
    title: String,
    leadingIcon: ImageVector? = null,
    onLeadingClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    LafyuuTopBar(
        modifier = modifier,
        leading = {
            leadingIcon?.let {
                IconClick(
                    icon = leadingIcon,
                    onClick = onLeadingClick,
                    contentDescription = contentDescription
                )
            }
        }
    ) {
        LafyuuText(
            text = title,
            style = Theme.typography.heading4,
            color = Theme.color.neutralDark,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SingleTopAppBarPreview() {
    SingleTopAppBar(
        title = "Favourite Products",
        onLeadingClick = {},
        leadingIcon = Left,
        contentDescription = "Back"
    )
}