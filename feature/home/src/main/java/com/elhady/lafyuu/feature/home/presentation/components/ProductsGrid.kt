package com.elhady.lafyuu.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.home.presentation.model.ProductUiModel

@Composable
fun ProductsGrid(
    products: List<ProductUiModel>,
    onProductClick: (productId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = Theme.space.large),
        horizontalArrangement = Arrangement.spacedBy(Theme.space.medium),
        verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        items(products, key = { it.id }) { product ->
            ProductCardItem(product = product, onClick = onProductClick)
        }
    }
}