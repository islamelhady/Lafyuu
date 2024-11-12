package com.elhady.lafyuu.feature.home.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.feature.home.R
import com.elhady.lafyuu.feature.home.presentation.model.ProductUiModel

@Composable
fun ProductCardItem(
    product: ProductUiModel,
    onClick: (productId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ProductCard(
        imageUrl = product.imageUrl,
        name = product.name,
        price = product.price,
        originalPrice = product.originalPrice,
        discountLabel = product.discountPercentage?.let { stringResource(R.string.home_discount_off, it) },
        rating = product.rating,
        modifier = modifier,
        onClick = { onClick(product.id) }
    )
}