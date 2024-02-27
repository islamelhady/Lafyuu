package com.elhady.lafyuu.core.designsystem.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.theme.LafyuuColor
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography
import java.lang.reflect.Modifier

@Composable
fun LafyuuPriceText(
    price: String,
    modifier: Modifier = Modifier,
    oldPrice: String? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = price,
            style = LafyuuTypography.Price,
            color = LafyuuColor.TextPrimary
        )

        oldPrice?.let {
            Text(
                text = it,
                style = LafyuuTypography.PriceOld,
                color = LafyuuColor.TextTertiary,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}

@Preview
@Composable
fun LafyuuPriceTextPreview() {
    LafyuuPriceText(
        price = "$100.00",
        oldPrice = "$150.00"
    )
}