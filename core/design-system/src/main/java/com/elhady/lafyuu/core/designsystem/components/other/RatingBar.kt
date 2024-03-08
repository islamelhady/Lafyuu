package com.elhady.lafyuu.core.designsystem.components.other

import Star
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Notification
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import kotlin.math.roundToInt


@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    iconSize: Dp = Theme.size.iconSmall,
    compact: Boolean = false,
    maxStar: Int = 5
) {
    Row(modifier) {
        repeat(maxStar) { index ->
            Icon(
                imageVector = Star,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = if (index < rating.roundToInt()) Theme.color.yellow else Theme.color.neutralLight
            )
        }
        if (!compact) {
            Text(
                text = " ${"%.1f".format(rating)}",
                style = Theme.typography.largeCaptionRegular12,
                color = Theme.color.neutralGrey
            )
        }
    }
}

@Preview
@Composable
private fun RatingBarPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SectionTitle("Rating")

                RatingBar(rating = 4.0f, iconSize = Theme.size.iconSmall)
                RatingBar(rating = 4.0f, iconSize = Theme.size.iconMedium)
                RatingBar(rating = 4.0f, iconSize = Theme.size.iconLarge, compact = true)

            }
        }
    }
}