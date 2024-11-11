package com.elhady.lafyuu.feature.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.R
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiIntent
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiState

@Composable
fun RecommendedSection(
    uiState: ProductDetailsUiState,
    onIntent: (ProductDetailsUiIntent) -> Unit
) {
    if (uiState.recommendedProducts.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
        SectionTitle(stringResource(R.string.product_you_might_also_like))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Theme.space.large),
            contentPadding = PaddingValues(vertical = Theme.space.small)
        ) {
            items(items = uiState.recommendedProducts, key = { it.id }) { item ->
                ProductCard(
                    imageUrl = item.imageUrl,
                    name = item.name,
                    price = item.price,
                    originalPrice = item.originalPrice,
                    discountLabel = if (item.discountPercentage > 0) {
                        stringResource(R.string.product_discount_label, item.discountPercentage)
                    } else null,
                    rating = item.rating,
                    onClick = {
                        onIntent(ProductDetailsUiIntent.RecommendedProductClicked(item.id))
                    }
                )
            }
        }
    }
}