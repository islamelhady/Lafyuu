package com.elhady.lafyuu.core.designsystem.components.card

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@SuppressLint("DefaultLocale")
@Composable
fun OrderCard(
    orderCode: String,
    updatedAt: String?,
    status: String,
    itemsCount: Int? = null,
    totalPrice: Double,
    paymentMethod: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(Theme.space.large),
        shape = Theme.corner.small,
        colors = CardDefaults.cardColors(containerColor = Theme.color.backgroundWhite),
        border = BorderStroke(width = Theme.size.border, color = Theme.color.neutralLight)
    ) {
        Column(
            modifier = modifier.padding(Theme.space.large),
            verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            LafyuuText(
                text = orderCode,
                style = Theme.typography.heading5,
                color = Theme.color.neutralDark,
            )
            LafyuuText(
                text = "Order at Lafyuu : $updatedAt",
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
                label = "Payment Method",
                value = paymentMethod
            )
            InfoRow(
                label = "Price",
                value = "$${String.format("%.2f", totalPrice)}"
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 600)
@Composable
private fun OrderCardPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                SectionTitle("Orders")
                OrderCard(
                    orderCode = "LQNSU346JK",
                    status = "Shipping",
                    itemsCount = 2,
                    onClick = {},
                    paymentMethod = "Credit Card",
                    totalPrice = 299.43,
                    updatedAt = "August 1, 2017"
                )

                OrderCard(
                    orderCode = "LQNSU346JK",
                    status = "Arriving",
                    itemsCount = 2,
                    paymentMethod = "Credit Card",
                    totalPrice = 299.43,
                    updatedAt = "August 1, 2017",
                    onClick = {}
                )
            }
        }
    }
}