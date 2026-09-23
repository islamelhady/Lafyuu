package com.elhady.lafyuu.core.designsystem.components.appbar

import Left
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.More
import com.elhady.lafyuu.core.designsystem.icons.Plus
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ProductTopBar(
    modifier: Modifier = Modifier,
    title: String,
    leadingIcon: ImageVector? = null,
    onLeadingClick: () -> Unit,
    trailingIcon: ImageVector? = null,
    onTrailingClick: () -> Unit,
    searchIcon: ImageVector? = null,
    onSearchClick: () -> Unit = {},
    trailingIconTint: Color? = null,
    contentDescription: String? = null,
) {
    LafyuuTopBar(
        modifier = modifier,
        leading = {
            leadingIcon?.let {
                IconClick(
                    icon = it,
                    onClick = onLeadingClick
                )
            }
        },
        trailing = listOf(
            {
                searchIcon?.let {
                    IconClick(
                        icon = it,
                        onClick = onSearchClick,
                    )
                }

                trailingIcon?.let {
                    IconClick(
                        icon = it,
                        onClick = onTrailingClick,
                        contentDescription = contentDescription,
                        tint = trailingIconTint ?: Theme.color.neutralGrey
                    )
                }
            }
        )
    ) {
        LafyuuText(
            text = title,
            style = Theme.typography.heading4,
            color = Theme.color.neutralDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductTopBarPreview() {
    Column(
        Modifier.padding(Theme.space.large)
    ) {
        ProductTopBar(
            title = "Nike Air Max 270 Rea",
            onLeadingClick = {},
            leadingIcon = Left,
            trailingIcon = More,
            onTrailingClick = {},
            searchIcon = Search,
            onSearchClick = {},
            contentDescription = null,
        )

        ProductTopBar(
            title = "Ship to",
            onLeadingClick = {},
            leadingIcon = Left,
            trailingIcon = Plus,
            trailingIconTint = Theme.color.blue,
            onTrailingClick = {},
            contentDescription = null,
        )
    }
}