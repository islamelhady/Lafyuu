package com.elhady.lafyuu.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.home.domain.model.Product
import java.util.Locale

@Composable
fun SaleProductSection(
    title: String,
    products: List<Product>,
    onProductClick: (productId: String) -> Unit,
    onSeeMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (products.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(
            title = title,
            actionText = "See More",
            onActionClick = onSeeMoreClick
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = Theme.space.large),
            horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            items(
                items = products
            ) { product ->
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
                    onClick = { onProductClick(product.id) }
                )
            }
        }
    }
}
