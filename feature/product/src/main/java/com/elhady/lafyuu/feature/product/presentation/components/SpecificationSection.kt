package com.elhady.lafyuu.feature.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.R
import com.elhady.lafyuu.feature.product.presentation.model.ProductDetailsUi

@Composable
fun SpecificationSection(product: ProductDetailsUi) {
    Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
        SectionTitle(stringResource(R.string.product_specification))
        if (product.productCode.isNotBlank()) {
            SpecificationRow(stringResource(R.string.product_style_label), product.productCode)
        }
        SpecificationRow(
            label = stringResource(R.string.product_stock_label),
            value = stringResource(R.string.product_stock_items, product.stock)
        )
        if (product.description.isNotBlank()) {
            LafyuuText(
                text = product.description,
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
                modifier = Modifier.padding(top = Theme.space.small)
            )
        }
    }
}