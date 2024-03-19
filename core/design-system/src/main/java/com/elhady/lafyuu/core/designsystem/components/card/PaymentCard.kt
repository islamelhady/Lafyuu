package com.elhady.lafyuu.core.designsystem.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Paypal
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

data class PaymentCardData(
    val cardNumberGroups: List<String>,
    val holderName: String,
    val expiryDate: String
)

@Composable
fun PaymentCard(
    card: PaymentCardData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(Theme.corner.small)
            .background(Theme.color.blue)
            .padding(Theme.space.extraLarge),
        verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        Icon(
            imageVector = Paypal,
            contentDescription = "Paypal",
        )
        LafyuuText(
            text = card.cardNumberGroups.joinToString("   "),
            style = Theme.typography.largeTextBold,
            color = Theme.color.backgroundWhite
        )
        Spacer(Modifier.height(Theme.space.extraLarge))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Theme.space.huge)
        ) {
            Column {
                LafyuuText(
                    "CARD HOLDER",
                    style = Theme.typography.largeCaptionRegular,
                    color = Theme.color.backgroundWhite
                )
                LafyuuText(
                    card.holderName,
                    style = Theme.typography.largeTextBold,
                    color = Theme.color.backgroundWhite
                )
            }
            Column {
                LafyuuText(
                    "CARD SAVE",
                    style = Theme.typography.largeCaptionRegular,
                    color = Theme.color.backgroundWhite
                )
                LafyuuText(
                    card.expiryDate,
                    style = Theme.typography.largeTextBold,
                    color = Theme.color.backgroundWhite
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 400)
@Composable
private fun PaymentCardPreview() {
    LafyuuTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            SectionTitle("Payment Card")
            PaymentCard(
                card = PaymentCardData(
                    cardNumberGroups = listOf("6326", "9124", "8124", "9875"),
                    holderName = "Islam Elhady",
                    expiryDate = "15/2030"
                )
            )
        }
    }
}