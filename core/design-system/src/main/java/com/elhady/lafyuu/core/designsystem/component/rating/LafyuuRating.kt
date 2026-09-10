package com.elhady.lafyuu.core.designsystem.component.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography
import kotlin.math.roundToInt

@Composable
fun LafyuuRating(
    rating: Float,
    maxRating: Int = 5,
    showValue: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val clampedRating = rating.coerceIn(0f, maxRating.toFloat())
    val filledStars = clampedRating.roundToInt()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(maxRating) { index ->
            Icon(
                imageVector = if (index < filledStars) {
                    Icons.Default.Star
                } else {
                    Icons.Outlined.StarBorder
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        if (showValue) {
            Text(
                text = String.format("%.1f", clampedRating),
                style = LafyuuTypography.Caption,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
fun LafyuuRatingPreview() {
    LafyuuRating(rating = 4.5f, showValue = true)
}