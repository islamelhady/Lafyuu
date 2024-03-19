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
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ShippingCard(
    dateShipping: String,
    shippingMethod: String,
    trackingNumber: String,
    address: String,
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
            label = "Date Shipping", dateShipping
        )
        InfoRow(
            label = "Shipping", shippingMethod
        )
        InfoRow(
            label = "No. Resi", trackingNumber
        )
        LafyuuText(
            text = "Address",
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralGrey,
        )
        LafyuuText(
            text = address,
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralDark,
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

                SectionTitle("Shipping")
                ShippingCard(
                    dateShipping = "January 16, 2015",
                    shippingMethod = "POS Reggular",
                    trackingNumber = "000192848573",
                    address = "2727 Lakeshore Rd undefined Nampa, Tennessee 78410"
                )
            }
        }
    }
}