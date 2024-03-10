package com.elhady.lafyuu.core.designsystem.components.card

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

data class PriceDetailsData(
    val itemsCount: Int,
    val itemsTotal: String,
    val shipping: String,
    val importCharges: String,
    val total: String
)

@Composable
fun PriceDetailsCard(
    details: PriceDetailsData,
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
        InfoRow(
            label = "Items (${details.itemsCount})",
            value = details.itemsTotal
        )
        InfoRow(
            label = "Shipping",
            value = details.shipping
        )
        InfoRow(
            label = "Import charges",
            value = details.importCharges
        )
        DashedDivider()
        InfoRow(
            label = "Total Price",
            value = details.total,
            isTotal = true
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


                SectionTitle("Price Details")
                PriceDetailsCard(
                    details = PriceDetailsData(
                        itemsCount = 3,
                        itemsTotal = "$598.86",
                        shipping = "$40.00",
                        importCharges = "$128.00",
                        total = "$766.86"
                    )
                )

            }
        }
    }
}