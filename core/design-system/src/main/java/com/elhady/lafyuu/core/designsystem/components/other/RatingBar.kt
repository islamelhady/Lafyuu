package com.elhady.lafyuu.core.designsystem.components.other

import Star
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import kotlin.math.roundToInt


@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    iconSize: Dp = Theme.size.iconSmall,
    maxStar: Int = 5,
    showRatingText: Boolean = false,
    onRatingChanged: ((Float) -> Unit)? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(maxStar) { index ->
            val starNumber = index + 1
            val isSelected = index < rating.roundToInt()
            Icon(
                imageVector = Star,
                contentDescription = "$starNumber stars",
                modifier = Modifier
                    .size(iconSize)
                    .then(
                        if (onRatingChanged != null) {
                            Modifier
                                .padding(4.dp)
                                .clickable { onRatingChanged(starNumber.toFloat()) }
                        } else {
                            Modifier
                        }
                    ),
                tint = if (isSelected) Theme.color.yellow else Theme.color.neutralLight
            )
        }
        if (showRatingText) {
            LafyuuText(
                text = "${rating.roundToInt()}/$maxStar",
                style = Theme.typography.heading5,
                color = Theme.color.neutralGrey,
                modifier = Modifier.padding(start = Theme.space.large)
            )
        }
    }
}

@Preview
@Composable
private fun RatingBarPreview() {
    LafyuuTheme {
        Surface() {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SectionTitle("Rating")

                RatingBar(rating = 4.0f, iconSize = Theme.size.iconSmall)
                RatingBar(rating = 4.0f, iconSize = Theme.size.iconMedium)
                RatingBar(rating = 4.0f, iconSize = Theme.size.iconLarge, showRatingText = true)
            }
        }
    }
}