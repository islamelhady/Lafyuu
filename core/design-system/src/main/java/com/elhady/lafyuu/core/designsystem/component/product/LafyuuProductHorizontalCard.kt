package com.elhady.lafyuu.core.designsystem.component.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elhady.lafyuu.core.designsystem.component.rating.LafyuuRating
import com.elhady.lafyuu.core.designsystem.theme.LafyuuDimens
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuProductHorizontalCard(
    imageUrl: String?,
    title: String,
    price: String,
    oldPrice: String? = null,
    discount: String? = null,
    rating: Float? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(LafyuuDimens.Space4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            modifier = Modifier
                .size(110.dp)
                .clip(
                    RoundedCornerShape(LafyuuDimens.Space4)
                ),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                LafyuuDimens.Space4
            ),
        ) {
            Text(
                text = title,
                style = LafyuuTypography.Body,
                maxLines = 2,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    LafyuuDimens.Space4
                ),
            ) {
                Text(
                    text = price,
                    style = LafyuuTypography.Price,
                )

                if (!oldPrice.isNullOrBlank()) {
                    Text(
                        text = oldPrice,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = LafyuuTypography.Caption.copy(
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                        ),
                    )
                }
            }

            if (!discount.isNullOrBlank()) {
                LafyuuDiscountBadge(text = discount)
            }

            if (rating != null) {
                LafyuuRating(
                    rating = rating,
                    showValue = true,
                )
            }
        }
    }
}

@Preview
@Composable
fun LafyuuProductHorizontalCardPreview() {
    LafyuuProductHorizontalCard(
        imageUrl = null,
        title = "Sample Product",
        price = "$19.99",
        oldPrice = "$29.99",
        discount = "20% OFF",
        rating = 4.5f,
        onClick = {},
    )
}