package com.elhady.lafyuu.core.designsystem.components.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun PriceText(
    price: String,
    modifier: Modifier = Modifier,
    oldPrice: String? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = price,
            style = Theme.typography.mediumTextBold,
            color = Theme.color.blue
        )

        oldPrice?.let {
            Text(
                text = it,
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}