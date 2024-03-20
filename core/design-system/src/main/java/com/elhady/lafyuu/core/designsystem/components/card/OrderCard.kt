package com.elhady.lafyuu.core.designsystem.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun OrderCard(
    orderCode: String,
    orderedAtLabel: String,
    status: String,
    itemsCount: Int,
    price: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(Theme.corner.small)
            .padding(Theme.space.large),
        verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        LafyuuText(
            text = orderCode,
            style = Theme.typography.heading5,
            color = Theme.color.neutralDark,
        )
        LafyuuText(
            text = orderedAtLabel,
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralGrey,
        )
        DashedDivider()

        InfoRow(
            label = "Order Status",
            value = status
        )
        InfoRow(
            label = "Items",
            value = "${itemsCount} Items purchased"
        )
        InfoRow(
            label = "Price",
            value = price
        )
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

                SectionTitle("Order")
                OrderCard(
                    orderCode = "LQNSU346JK",
                    orderedAtLabel = "Order at Lafyuu : August 1, 2017",
                    status = "Shipping",
                    itemsCount = 2,
                    price = "$299,43"
                )
            }
        }
    }
}