package com.elhady.lafyuu.core.designsystem.components.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Transaction
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun NotificationListItem(
    title: String,
    description: String,
    date: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    image: Painter? = null,
    leadingIcon: ImageVector? = null,
    backgroundColor: Color = Theme.color.backgroundWhite
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(all = Theme.space.large),
        verticalAlignment = Alignment.Top
    ) {

        leadingIcon?.let {
            Icon(
                imageVector = leadingIcon,
                modifier = Modifier
                    .align(Alignment.Top),
                contentDescription = null,
                tint = Theme.color.blue,
            )
        }

        image?.let {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.size(Theme.size.large)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = Theme.space.medium),
            verticalArrangement = Arrangement.spacedBy(Theme.space.extraSmall)
        ) {
            LafyuuText(
                text = title,
                style = Theme.typography.heading6,
                color = Theme.color.neutralDark
            )
            LafyuuText(
                text = description,
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey
            )
            LafyuuText(
                text = date,
                style = Theme.typography.normalCaptionRegular,
                color = Theme.color.neutralGrey
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 400)
@Composable
private fun AllCardAndListComponentsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                SectionTitle("Notification List")
                NotificationListItem(
                    title = "Transaction Nike Air Zoom Product",
                    description = "Culpa cillum consectetur labore nulla nulla magna irure. Id veniam culpa officia aute dolor amet deserunt ex proident commodo",
                    date = "April 30, 2014 1:01 PM",
                    onClick = {},
                    leadingIcon = Transaction,
                )
                NotificationListItem(
                    title = "New Product",
                    description = "sint laborum amet proident",
                    date = "June 3, 2015 5:06 PM",
                    onClick = {},
                    image = painterResource(id = R.drawable.imp_product_shoes_yellow),
                )
            }
        }
    }
}
