package com.elhady.lafyuu.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.home.R
import com.elhady.lafyuu.feature.home.presentation.model.ProductUiModel

@Composable
fun SaleProductSection(
    title: String,
    products: List<ProductUiModel>,
    onProductClick: (productId: String) -> Unit,
    onSeeMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (products.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(
            title = title,
            actionText = stringResource(R.string.home_see_more),
            onActionClick = onSeeMoreClick
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = Theme.space.large),
            horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            items(items = products, key = { it.id }) { product ->
                ProductCardItem(product = product, onClick = onProductClick)
            }
        }
    }
}