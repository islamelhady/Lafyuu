package com.elhady.lafyuu.feature.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.R
import com.elhady.lafyuu.feature.product.presentation.model.ProductDetailsUi

@Composable
fun PriceRow(product: ProductDetailsUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        LafyuuText(
            text = product.price,
            style = Theme.typography.heading3,
            color = Theme.color.blue
        )
        product.originalPrice?.let { originalPrice ->
            LafyuuText(
                text = originalPrice,
                style = Theme.typography.normalTextRegular.copy(
                    textDecoration = TextDecoration.LineThrough
                ),
                color = Theme.color.neutralGrey
            )
        }
        if (product.discountPercentage > 0) {
            LafyuuText(
                text = stringResource(R.string.product_discount_off, product.discountPercentage),
                style = Theme.typography.normalTextBold,
                color = Theme.color.red
            )
        }
    }
}