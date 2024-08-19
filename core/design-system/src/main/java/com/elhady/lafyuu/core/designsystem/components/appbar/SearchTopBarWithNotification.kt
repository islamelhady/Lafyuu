package com.elhady.lafyuu.core.designsystem.components.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.other.NotificationMark
import com.elhady.lafyuu.core.designsystem.components.text.HintText
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.icons.Notification
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SearchTopBarWithNotification(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    onLeadingClick: () -> Unit,
    onSearchClick: () -> Unit,
    trailingIcon: ImageVector? = null,
    onTrailingClick: () -> Unit,
    hasNotification: Boolean = false,
    onNotificationClick: () -> Unit
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
                trailingIcon?.let {
                    IconClick(
                        icon = trailingIcon,
                        onClick = onTrailingClick
                    )
                }
                NotificationMark(
                    hasNotification = hasNotification,
                    onClick = onNotificationClick,
                    icon = Notification
                )
            }
        )
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(Theme.size.inputFieldHeight)
                .clip(shape = Theme.corner.small)
                .border(
                    width = Theme.size.border,
                    color = Theme.color.neutralLight,
                    shape = Theme.corner.small
                )
                .clickable(onClick = onSearchClick)
                .background(Theme.color.backgroundWhite),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Theme.space.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Theme.space.small)
            ) {
                Icon(
                    imageVector = Search,
                    contentDescription = "Search",
                    tint = Theme.color.blue
                )
                HintText(
                    text = "Search Product"
                )
            }
        }
    }
}

@Composable
internal fun IconClick(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Theme.color.neutralGrey,
    contentDescription: String? = null
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            modifier = Modifier,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchTopBarWithNotificationPreview() {
    SearchTopBarWithNotification(
        trailingIcon = Love,
        hasNotification = true,
        onLeadingClick = {},
        onTrailingClick = {},
        onNotificationClick = {},
        onSearchClick = {}
    )
}