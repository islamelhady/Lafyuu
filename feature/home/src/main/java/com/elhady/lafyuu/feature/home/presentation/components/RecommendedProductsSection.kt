package com.elhady.lafyuu.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.card.Banner
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.home.domain.model.Product
import java.util.Locale

@Composable
fun RecommendedProductsSection(
    products: List<Product>,
    onProductClick: (productId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (products.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.space.large),
        verticalArrangement = Arrangement.spacedBy(Theme.space.large)
    ) {
        Banner(
            title = "Recommended Products",
            subtitle = "We recommend the best for you",
            imageUrl = R.drawable.img_promo_shoes_close_up,
        )
        val pairs = products.chunked(2)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.space.large),
            verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            pairs.forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
                ) {
                    pair.forEach { product ->
                        val formattedPrice = String.format(Locale.US, "$%.2f", product.price)
                        val formattedOriginalPrice = product.originalPrice?.let {
                            String.format(Locale.US, "$%.2f", it)
                        }

                        ProductCard(
                            imageUrl = product.coverPictureUrl,
                            name = product.name,
                            price = formattedPrice,
                            originalPrice = formattedOriginalPrice,
                            discountLabel = product.discountLabel,
                            rating = product.rating,
                            modifier = Modifier.weight(1f),
                            onClick = { onProductClick(product.id) }
                        )
                    }
                    if (pair.size == 1) {
                        Row(modifier = Modifier.weight(1f)) {}
                    }
                }
            }
        }
    }
}
