package com.elhady.lafyuu.core.designsystem.components.appbar

import Left
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.other.NotificationMark
import com.elhady.lafyuu.core.designsystem.components.search.SearchBar
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.icons.Notification
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SearchTopBarWithNotification(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    onLeadingClick: () -> Unit,
    onClearSearchClick: () -> Unit,
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
        SearchBar(
            value = searchValue,
            onValueChange = onSearchValueChange,
            onClear = onClearSearchClick,
            modifier = Modifier.weight(1f)
        )
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
    var search by remember { mutableStateOf("") }
    SearchTopBarWithNotification(
        searchValue = search,
        onSearchValueChange = { search = it },
        onClearSearchClick = { search = "" },
        leadingIcon = Left,
        trailingIcon = Love,
        hasNotification = true,
        onLeadingClick = {},
        onTrailingClick = {},
        onNotificationClick = {},
    )
}