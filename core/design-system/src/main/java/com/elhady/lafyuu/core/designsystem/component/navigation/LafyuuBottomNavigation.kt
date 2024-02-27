package com.elhady.lafyuu.core.designsystem.component.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun LafyuuBottomNavigation(
    items: List<LafyuuNavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        items.forEachIndexed { index, item ->

            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    onItemSelected(index)
                },
                icon = {
                    Box {
                        Icon(
                            imageVector = if (selectedIndex == index) {
                                item.selectedIcon
                            } else {
                                item.icon
                            },
                            contentDescription = stringResource(
                                item.labelRes
                            ),
                        )

                        if (item.badgeCount >  0) {
                            LafyuuNavigationBadge(
                                count = item.badgeCount,
                                modifier = Modifier
                                    .align(
                                        Alignment.TopEnd
                                    ),
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = stringResource(item.labelRes)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor =
                        MaterialTheme.colorScheme.primary,
                    selectedTextColor =
                        MaterialTheme.colorScheme.primary,
                    unselectedIconColor =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor =
                        MaterialTheme.colorScheme.surface,
                ),
            )
        }
    }
}
