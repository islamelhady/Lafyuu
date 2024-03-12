package com.elhady.lafyuu.core.designsystem.components.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextIndent
import com.elhady.lafyuu.core.designsystem.components.other.NotificationMark
import com.elhady.lafyuu.core.designsystem.components.search.SearchBar
import com.elhady.lafyuu.core.designsystem.icons.Notification
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SearchHeader(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    onLeadingClick: () -> Unit,
    onClearSearch: () -> Unit,
    trailingIcon: ImageVector? = null,
    onTrailingClick: () -> Unit,
    hasNotification: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Theme.space.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Theme.space.small)
    ) {
        leadingIcon?.let {
            IconClick(
                icon = leadingIcon,
                onClick = onLeadingClick
            )
        }
        SearchBar(
            value = searchValue,
            onValueChange = onSearchValueChange,
            onClear = onClearSearch,
            modifier = Modifier.weight(1f)
        )
        trailingIcon?.let {
            IconClick(
                icon = trailingIcon,
                onClick = onTrailingClick
            )
        }
        NotificationMark(
            hasNotification = hasNotification,
            onClick = onTrailingClick,
        ) {
            IconClick(
                icon = Notification,
                onClick = onTrailingClick
            )
        }
    }
}

@Composable
fun IconClick(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Theme.color.neutralGrey
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            modifier = modifier,
            contentDescription = "Icon Top Bar",
            tint = tint
        )
    }
}