package com.elhady.lafyuu.core.designsystem.components.appbar

import Left
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.More
import com.elhady.lafyuu.core.designsystem.icons.Plus
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ProductTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    leadingIcon: ImageVector? = null,
    onLeadingClick: () -> Unit,
    trailingIcon: ImageVector? = null,
    onTrailingClick: () -> Unit,
    searchIcon: ImageVector? = null,
    onSearchClick: () -> Unit = {},
    contentDescription: String? = null,
) {
    LafyuuAppBar(
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
                        contentDescription = contentDescription
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
fun ProductTopAppBarPreview() {
    Column(
        Modifier.padding(Theme.space.large)
    ) {
        ProductTopAppBar(
            title = "Nike Air Max 270 Rea",
            onLeadingClick = {},
            leadingIcon = Left,
            trailingIcon = More,
            onTrailingClick = {},
            searchIcon = Search,
            onSearchClick = {},
            contentDescription = null,
        )

        ProductTopAppBar(
            title = "Ship to",
            onLeadingClick = {},
            leadingIcon = Left,
            trailingIcon = Plus,
            onTrailingClick = {},
            contentDescription = null,
        )
    }
}