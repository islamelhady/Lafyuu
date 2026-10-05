package com.elhady.lafyuu.core.designsystem.components.card

import androidx.compose.foundation.border
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
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun PriceDetails(
    itemsCount: Int? = null,
    itemsTotal: String,
    shipping: String,
    discount: String,
    importCharges: String,
    totalPrice: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(Theme.corner.small)
            .border(
                width = Theme.size.border,
                color = Theme.color.neutralLight,
                shape = Theme.corner.small
            )
            .padding(Theme.space.large),
        verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        itemsCount?.let {
            InfoRow(
                label = "Items (${itemsCount})",
                value = itemsTotal
            )
        }
        InfoRow(
            label = "Shipping",
            value = shipping
        )
        InfoRow(
            label = "Import charges",
            value = importCharges
        )
        InfoRow(
            label = "Discount",
            value = discount
        )
        DashedDivider()
        InfoRow(
            label = "Total Price",
            value = totalPrice,
            isTotal = true
        )
    }
}


@Preview(showBackground = true, widthDp = 380, heightDp = 400)
@Composable
private fun PriceDetailsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {


                SectionTitle("Price Details")
                PriceDetails(
                    itemsCount = 3,
                    itemsTotal = "$598.86",
                    shipping = "$40.00",
                    importCharges = "$128.00",
                    discount = "$0.00",
                    totalPrice = "$766.86"
                )
            }
        }
    }
}