package com.elhady.lafyuu.core.designsystem.components.element

import Home
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
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
    items: List<TabBarItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Theme.color.backgroundWhite)
            .padding(vertical = Theme.space.small),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = index == selectedIndex
            val tint = if (isSelected) Theme.color.blue else Theme.color.neutralGrey
            val textStyle = if (isSelected) Theme.typography.normalTextBold else Theme.typography.normalTextRegular

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onItemSelected(index) }
            ) {
                Box {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = tint
                    )
                    if (item.badgeCount != null && item.badgeCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 9.dp, y = (-9).dp)
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Theme.color.red)
                                .border(
                                    width = 2.dp,
                                    color = Theme.color.backgroundWhite,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            LafyuuText(
                                text = item.badgeCount.toString(),
                                style = Theme.typography.normalTextBold,
                                color = Theme.color.backgroundWhite
                            )
                        }
                    }
                }
                Spacer(Modifier.height(Theme.space.extraExtraSmall))
                LafyuuText(
                    item.label,
                    style = textStyle,
                    color = tint
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 200)
@Composable
private fun AllElementComponentsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {

                SectionTitle("Tab Bar")
                var selectedTab by remember { mutableIntStateOf(0) }
                NavigationBottomBar(
                    items = defaultTabBarItems,
                    selectedIndex = selectedTab,
                    onItemSelected = { selectedTab = it }
                )

            }
        }
    }
}