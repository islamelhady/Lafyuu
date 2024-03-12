package com.elhady.lafyuu.core.designsystem.components.appbar

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.other.NotificationMark
import com.elhady.lafyuu.core.designsystem.components.search.SearchBar
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.icons.Mic
import com.elhady.lafyuu.core.designsystem.icons.Notification
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme


@Composable
internal fun LafyuuAppBar(
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    trailing: List<@Composable () -> Unit> = emptyList(),
    center: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Theme.space.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Theme.space.small)
    ) {
        leading?.invoke()
        center()
        trailing.forEach { it() }
    }
}


@Preview(showBackground = true, widthDp = 380, heightDp = 500)
@Composable
private fun AllHeaderCasesPreview() {
    LafyuuTheme {
        Surface {
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {

                var search1 by remember { mutableStateOf("") }

                SearchBarWithNotification(
                    searchValue = search1,
                    onSearchValueChange = { search1 = it },
                    onClearSearchClick = { search1 = "" },
                    leadingIcon = Left,
                    onLeadingClick = {},
                    trailingIcon = Love,
                    onTrailingClick = {},
                    hasNotification = true,
                    onNotificationClick = {}
                )
                LafyuuAppBar(
                    leading = {
                        IconClick(
                            icon = Left,
                            onClick = {}
                        )
                    },
                    trailing = listOf(
                        {
                            IconClick(
                                Love,
                                onClick = {})
                        },
                        {
                            NotificationMark(
                                hasNotification = true,
                                onClick = {}
                            ) {
                                Icon(
                                    Notification,
                                    contentDescription = null,
                                    tint = Theme.color.neutralGrey
                                )
                            }
                        }
                    )
                ) {
                    SearchBar(
                        value = search1,
                        onValueChange = { search1 = it },
                        onClear = { search1 = "" },
                        modifier = Modifier.weight(1f)
                    )
                }
                Divider(color = Theme.color.neutralLight)

                // 2) Title + Back + heart + search ("< Super Flash Sale")
                LafyuuAppBar(
                    leading = {
                        IconClick(
                            icon = Left,
                            onClick = {})
                    },
                    trailing = listOf(
                        {
                            IconClick(
                                icon = Love,
                                onClick = {})
                        },
                        {
                            IconClick(
                                icon = Search,
                                onClick = {})
                        }
                    )
                ) {
                    LafyuuText(
                        text = "Super Flash Sale",
                        style = Theme.typography.heading4,
                        color = Theme.color.neutralDark,
                        modifier = Modifier.weight(1f)
                    )
                }
                Divider(color = Theme.color.neutralLight)

                LafyuuAppBar {
                    LafyuuText(
                        text = "Single",
                        style = Theme.typography.heading4,
                        color = Theme.color.neutralDark,
                        modifier = Modifier.weight(1f)
                    )
                }
                Divider(color = Theme.color.neutralLight)

                LafyuuAppBar(
                    leading = {}
                ) {
                    LafyuuText(
                        text = "Favourite Product",
                        style = Theme.typography.heading4,
                        color = Theme.color.neutralDark,
                        modifier = Modifier.weight(1f)
                    )
                }

                Divider(color = Theme.color.neutralLight)
                var search4 by remember { mutableStateOf("Nike Air Max") }
                LafyuuAppBar(
                    trailing = listOf(
                        {
                            IconClick(
                                icon = Notification,
                                onClick = {})
                        }
                    )
                ) {
                    SearchBar(
                        value = search4,
                        onValueChange = { search4 = it },
                        onClear = { search4 = "" },
                        modifier = Modifier.weight(1f)
                    )
                }
                Divider(color = Theme.color.neutralLight)

                var search5 by remember { mutableStateOf("") }
                LafyuuAppBar(
                    trailing = listOf(
                        {
                            IconClick(
                                icon = Mic,
                                onClick = {})
                        },
                    )
                ) {
                    SearchBar(
                        value = search5,
                        onValueChange = { search5 = it },
                        onClear = { search5 = "" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}