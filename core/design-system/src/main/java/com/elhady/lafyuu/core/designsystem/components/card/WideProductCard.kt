package com.elhady.lafyuu.core.designsystem.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.appbar.IconClick
import com.elhady.lafyuu.core.designsystem.components.button.QuantityButton
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.icons.LoveFilled
import com.elhady.lafyuu.core.designsystem.icons.Trash
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun WideProductCard(
    imageUrl: Int,
    name: String,
    price: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    quantityButton: @Composable (() -> Unit)? = null
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = Theme.corner.small,
        colors = CardDefaults.cardColors(containerColor = Theme.color.backgroundWhite),
        border = BorderStroke(1.dp, Theme.color.neutralLight)
    ) {
        Row(
            modifier = Modifier
                .padding(Theme.space.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            Image(
                painter = painterResource(id = imageUrl),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(Theme.size.huge)
                    .clip(Theme.corner.small)
                    .background(Theme.color.neutralLight),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .height(Theme.size.huge),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    LafyuuText(
                        text = name,
                        style = Theme.typography.heading6,
                        color = Theme.color.neutralDark,
                        maxLines = 2,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = Theme.space.medium)
                    )
                    IconClick(
                        icon = if (isFavorite) LoveFilled else Love,
                        onClick = onFavoriteClick,
                        tint = if (isFavorite) Theme.color.red else Theme.color.neutralGrey,
                        modifier = Modifier
                            .padding(end = Theme.space.small)
                            .size(Theme.size.iconMedium)
                    )
                    onDeleteClick?.let {
                        IconClick(
                            icon = Trash,
                            onClick = it,
                            tint = Theme.color.neutralGrey,
                            modifier = Modifier.size(Theme.size.iconMedium)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LafyuuText(
                        text = "$$price",
                        style = Theme.typography.heading6,
                        color = Theme.color.blue
                    )
                    quantityButton?.let {
                        it()
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun wideProductCardPreview() {
    LafyuuTheme {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var quantity by remember { mutableIntStateOf(1) }
            WideProductCard(
                imageUrl = R.drawable.img_product_shoes_blue,
                name = "Nike Air Zoom Pegasus 36 Miami",
                onDeleteClick = {},
                quantityButton = {
                    QuantityButton(
                        quantity = quantity,
                        onDecrement = { if (quantity > 1) quantity-- },
                        onIncrement = { quantity++ }
                    )
                },
                onClick = {},
                price = "299,43",
                isFavorite = false,
                onFavoriteClick = {}
            )

            var fav by remember { mutableStateOf(false) }
            WideProductCard(
                imageUrl = R.drawable.img_product_shoes_blue,
                name = "Nike Air Zoom Pegasus 36 Miami",
                price = "299,43",
                isFavorite = fav,
                onFavoriteClick = { fav = !fav },
                onClick = {},
            )
        }
    }
}