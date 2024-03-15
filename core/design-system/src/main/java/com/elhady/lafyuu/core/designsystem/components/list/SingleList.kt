package com.elhady.lafyuu.core.designsystem.components.list


import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.other.NotificationMarkCount
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Offer
import com.elhady.lafyuu.core.designsystem.icons.Right
import com.elhady.lafyuu.core.designsystem.icons.X
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme


@Composable
fun SingleListItem(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    titleStyle: TextStyle = Theme.typography.heading6,
    modifier: Modifier = Modifier,
    badgeCount: @Composable (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    leadingIconTint: Color = Theme.color.neutralGrey,
    showChevron: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Theme.size.buttonHeight)
            .clip(Theme.corner.small)
            .background(Theme.color.backgroundWhite)
            .clickable(onClick = onClick)
            .padding(all = Theme.space.large),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = leadingIconTint,
                modifier = Modifier
                    .padding(end = Theme.space.large)
                    .size(24.dp)
            )
        }

        LafyuuText(
            text = title,
            style = titleStyle,
            color = Theme.color.neutralDark,
            modifier = Modifier.weight(1f)
        )
        badgeCount?.let {
            badgeCount()
        }

        subtitle?.let {
            LafyuuText(
                text = subtitle,
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
            )
        }

        if (showChevron) {
            Icon(
                imageVector = Right,
                contentDescription = null,
                tint = Theme.color.neutralGrey,
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 380, heightDp = 500)
@Composable
private fun AllSingleListShapesPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                SingleListItem(
                    title = "List",
                    subtitle = "List",
                    showChevron = true,
                    onClick = {},
                    leadingIcon = Offer,
                    leadingIconTint = Theme.color.blue
                )

                SingleListItem(
                    title = "List",
                    leadingIcon = X,
                    onClick = {},
                )


                SingleListItem(
                    title = "List",
                    leadingIcon = X,
                    leadingIconTint = Theme.color.blue,
                    badgeCount = {
                        Box{
                            NotificationMarkCount(badgeCount = 2)
                        }
                    },
                    onClick = {}
                )

                SingleListItem(
                    title = "Single List",
                    onClick = {}
                )

                SingleListItem(
                    title = "Nike Air Max 270 React ENG",
                    onClick = {},
                    titleStyle = Theme.typography.normalTextRegular
                )
            }
        }
    }
}
