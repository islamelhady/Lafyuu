package com.elhady.lafyuu.core.designsystem.component.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.elhady.lafyuu.core.designsystem.component.rating.LafyuuRating
import com.elhady.lafyuu.core.designsystem.theme.LafyuuDimens
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuProductCard(
    imageUrl: String?,
    title: String,
    price: String,
    oldPrice: String? = null,
    discount: String? = null,
    rating: Float? = null,
    onClick: () -> Unit,
    onFavoriteClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(LafyuuDimens.Space2),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.78f)
                .clip(RoundedCornerShape(LafyuuDimens.Space4))
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
            )

            if (!discount.isNullOrBlank()) {
                LafyuuDiscountBadge(
                    text = discount,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(LafyuuDimens.Space4),
                )
            }

            if (onFavoriteClick != null) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(LafyuuDimens.Space2)
                        .background(
                            color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(50),
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to favorites",
                    )
                }
            }
        }

        Text(
            text = title,
            style = LafyuuTypography.Body,
            maxLines = 2,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(LafyuuDimens.Space2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = price,
                style = LafyuuTypography.Price,
            )

            if (!oldPrice.isNullOrBlank()) {
                Text(
                    text = oldPrice,
                    style = LafyuuTypography.Price.copy(
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                    ),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        if (rating != null) {
            LafyuuRating(
                rating = rating,
                showValue = true,
            )
        }
    }
}

@Preview
@Composable
fun LafyuuProductCardPreview() {
    LafyuuProductCard(
        imageUrl = null,
        title = "Sample Product",
        price = "$19.99",
        oldPrice = "$29.99",
        discount = "20% OFF",
        rating = 4.5f,
        onClick = {},
        onFavoriteClick = {},
    )
}