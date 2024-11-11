package com.elhady.lafyuu.feature.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.icons.LoveFilled
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.R
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiIntent
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiIntent.*
import com.elhady.lafyuu.feature.product.presentation.model.ProductDetailsUi

@Composable
fun ProductHeader(
    product: ProductDetailsUi,
    isFavorite: Boolean,
    onIntent: (ProductDetailsUiIntent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LafyuuText(
            text = product.name,
            style = Theme.typography.heading3,
            color = Theme.color.neutralDark,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { onIntent(FavoriteToggled) }) {
            Icon(
                imageVector = if (isFavorite) LoveFilled else Love,
                contentDescription = stringResource(R.string.product_favorite_cd),
                tint = if (isFavorite) Theme.color.red else Theme.color.neutralGrey
            )
        }
    }
}