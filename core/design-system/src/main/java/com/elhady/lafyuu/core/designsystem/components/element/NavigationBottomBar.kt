package com.elhady.lafyuu.core.designsystem.components.element

import Home
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.other.NotificationMarkCount
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Cart
import com.elhady.lafyuu.core.designsystem.icons.Offer
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.core.designsystem.icons.User
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

data class TabBarItem(
    val label: String,
    val icon: ImageVector,
    val badgeCount: Int? = null
)

val defaultTabBarItems = listOf(
    TabBarItem("Home", Home),
    TabBarItem("Explore", Search),
    TabBarItem("Cart", Cart, badgeCount = 2),
    TabBarItem("Offer", Offer),
    TabBarItem("Account", User)
)

@Composable
fun NavigationBottomBar(
    tabs: List<TabBarItem>,
    onTabSelected: (TabBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(2) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Theme.size.huge)
            .dropShadow(
                shape = Theme.corner.none,
                shadow = Shadow(
                    radius = 12.dp,
                    offset = DpOffset(x = 0.dp, y = (-4).dp),
                    color = Theme.color.blue.copy(alpha = 0.1f)
                )
            )
            .background(color = Theme.color.backgroundWhite)
    ) {
        tabs.forEachIndexed { index, tab ->
            TabItem(
                tab = tab,
                isSelected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    onTabSelected(tabs[index])
                }
            )
        }
    }
}

@Composable
private fun RowScope.TabItem(
    tab: TabBarItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = if (isSelected) Theme.color.blue else Theme.color.neutralGrey
    val contentStyle = if (isSelected) Theme.typography.normalTextBold else Theme.typography.normalTextRegular
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .clip(Theme.corner.small)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                imageVector = tab.icon,
                contentDescription = "${tab.label} tab icon",
                modifier = Modifier.size(Theme.size.iconLarge),
                colorFilter = ColorFilter.tint(contentColor)
            )
            if (tab.badgeCount != null && tab.badgeCount > 0) {
                NotificationMarkCount(
                    badgeCount = tab.badgeCount,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 9.dp, y = (-9).dp)
                )
            }
        }
        LafyuuText(
            text = tab.label,
            color = contentColor,
            style = contentStyle
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarPreview() {
    LafyuuTheme {
        Column(
            modifier = Modifier
                .background(Theme.color.backgroundWhite)
                .padding(top = 100.dp),
        ) {

            NavigationBottomBar(
                tabs = defaultTabBarItems,
                onTabSelected = {}
            )
        }
    }
}