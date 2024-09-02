package com.elhady.lafyuu.core.designsystem.components.card


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.appbar.IconClick
import com.elhady.lafyuu.core.designsystem.components.other.RatingBar
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Trash
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ProductCard(
    imageUrl: Any?,
    name: String,
    price: String,
    originalPrice: String? = null,
    discountLabel: String? = null,
    isDelete: Boolean? = null,
    rating: Int? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(165.dp),
        shape = Theme.corner.small,
        colors = CardDefaults.cardColors(containerColor = Theme.color.backgroundWhite),
        border = BorderStroke(1.dp, Theme.color.neutralLight)
    ) {
        Column(
            modifier = Modifier.padding(Theme.space.large)
        ) {
            AsyncImage(
                model = imageUrl ?: R.drawable.imp_product_shoes_yellow,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.imp_product_shoes_yellow),
                error = painterResource(R.drawable.imp_product_shoes_yellow),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(133.dp)
                    .clip(Theme.corner.small)
                    .background(Theme.color.neutralLight)
            )
            LafyuuText(
                text = name,
                style = Theme.typography.heading6,
                color = Theme.color.neutralDark,
                maxLines = 2,
                modifier = Modifier.padding(
                    top = Theme.space.small,
                    bottom = Theme.space.extraSmall
                )
            )
            rating?.let {
                RatingBar(
                    rating = it,
                    modifier = Modifier.padding(bottom = Theme.space.large)
                )
            }
            LafyuuText(
                text = price,
                style = Theme.typography.normalTextBold,
                color = Theme.color.blue
            )
            Row(
                modifier = Modifier.padding(top = Theme.space.extraSmall),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(Theme.space.extraSmall)
            ) {
                originalPrice?.let {
                    LafyuuText(
                        text = originalPrice,
                        style = Theme.typography.normalTextRegular.copy(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        color = Theme.color.neutralGrey
                    )
                }
                discountLabel?.let {
                    LafyuuText(
                        text = discountLabel,
                        style = Theme.typography.normalTextBold,
                        color = Theme.color.red,
                    )
                }
                isDelete?.let {
                    IconClick(
                        icon = Trash,
                        onClick = {},
                        tint = Theme.color.neutralGrey,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 900)
@Composable
private fun ProductCardsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(6.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                ProductCard(
                    imageUrl = painterResource(R.drawable.imp_product_shoes_yellow),
                    name = "Nike Air Zoom Pegasus 36 Miami",
                    price = "$299,43",
                    rating = 4,
                    discountLabel = "50% OFF",
                    originalPrice = "$399,43",
                    isDelete = true,
                    onClick = {}
                )

                ProductCard(
                    imageUrl = painterResource(R.drawable.imp_product_shoes_yellow),
                    name = "Nike Air Zoom Pegasus 36 Miami",
                    price = "$299,43",
                    discountLabel = "50% OFF",
                    originalPrice = "$399,43",
                    onClick = {}
                )

                ProductCard(
                    imageUrl = painterResource(R.drawable.imp_product_shoes_yellow),
                    name = "Nike Air Zoom Pegasus 36 Miami",
                    price = "$299,43",
                    rating = 4,
                    discountLabel = "50% OFF",
                    originalPrice = "$399,43",
                    onClick = {}

                )
            }
        }
    }
}
