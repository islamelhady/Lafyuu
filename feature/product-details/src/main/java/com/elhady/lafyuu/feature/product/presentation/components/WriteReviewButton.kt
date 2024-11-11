package com.elhady.lafyuu.feature.product.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.elhady.lafyuu.core.designsystem.components.button.LabelButton
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiIntent

@Composable
fun WriteReviewButton(
    captionRes: Int,
    onIntent: (ProductDetailsUiIntent) -> Unit
) {
    LabelButton(
        caption = stringResource(captionRes),
        onClick = { onIntent(ProductDetailsUiIntent.WriteReviewClicked) },
        contentColor = Theme.color.blue,
        hasBorder = true,
        borderColor = Theme.color.blue,
        style = Theme.typography.largeCaptionBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Theme.space.small)
    )
}