package com.elhady.lafyuu.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.elhady.lafyuu.core.designsystem.components.card.Banner
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.home.R
import com.elhady.lafyuu.feature.home.presentation.model.ProductUiModel
import com.elhady.lafyuu.core.designsystem.R as DesignR

@Composable
fun RecommendedProductsSection(
    products: List<ProductUiModel>,
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
            title = stringResource(R.string.home_recommended_title),
            subtitle = stringResource(R.string.home_recommended_subtitle),
            imageUrl = DesignR.drawable.img_promo_shoes_close_up
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            products.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
                ) {
                    pair.forEach { product ->
                        ProductCardItem(
                            product = product,
                            onClick = onProductClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}