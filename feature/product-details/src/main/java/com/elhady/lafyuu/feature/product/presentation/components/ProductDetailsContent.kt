package com.elhady.lafyuu.feature.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiIntent
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiState


@Composable
fun ProductDetailsContent(
    uiState: ProductDetailsUiState,
    onIntent: (ProductDetailsUiIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val product = uiState.product ?: return

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = Theme.space.medium)
    ) {
        ProductImagesPager(product)

        Column(
            modifier = Modifier.padding(horizontal = Theme.space.large),
            verticalArrangement = Arrangement.spacedBy(Theme.space.large)
        ) {
            ProductHeader(product, uiState.isFavorite, onIntent)
            PriceRow(product)
            SpecificationSection(product)
            ReviewsSection(uiState, onIntent)
            RecommendedSection(uiState, onIntent)
        }
    }
}
