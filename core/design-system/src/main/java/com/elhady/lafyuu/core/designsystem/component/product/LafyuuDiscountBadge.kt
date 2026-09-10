package com.elhady.lafyuu.core.designsystem.component.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuDiscountBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(6.dp),
            )
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
        color = MaterialTheme.colorScheme.onError,
        style = LafyuuTypography.Caption,
    )
}

@Preview
@Composable
fun LafyuuDiscountBadgePreview() {
    LafyuuDiscountBadge(text = "20% OFF")
}