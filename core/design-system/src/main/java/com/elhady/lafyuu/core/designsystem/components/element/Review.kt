package com.elhady.lafyuu.core.designsystem.components.element

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.other.RatingBar
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

data class ReviewData(
    val userName: String,
    @DrawableRes val avatarUrl: Int,
    val rating: Float,
    val comment: String,
    val date: String,
    @DrawableRes val imageUrls: List<Int> = emptyList()
)

@Composable
fun ReviewCard(
    review: ReviewData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Theme.space.large)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Theme.space.large),
        ) {
            Image(
                painter = painterResource(id = review.avatarUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(Theme.size.large)
                    .clip(CircleShape)
                    .background(Theme.color.neutralLight)
            )
            Column {
                LafyuuText(
                    text = review.userName,
                    style = Theme.typography.heading5,
                    color = Theme.color.neutralDark
                )
                RatingBar(
                    rating = review.rating,
                    iconSize = Theme.size.iconMedium,
                )
            }
        }
        LafyuuText(
            text = review.comment,
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralGrey
        )
        if (review.imageUrls.isNotEmpty()) {
            ImageReview(
                imageUrls = review.imageUrls,
                onAddClick = null
            )
        }
        LafyuuText(
            text = review.date,
            style = Theme.typography.normalCaptionRegular,
            color = Theme.color.neutralGrey
        )
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 400)
@Composable
private fun AllElementComponentsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                SectionTitle("Review")
                ReviewCard(
                    review = ReviewData(
                        userName = "James Lawson",
                        avatarUrl = R.drawable.img_profile_man_bearded,
                        rating = 3.0f,
                        comment = "air max are always very comfortable fit, clean and just perfect in every way. just the box was too small and scrunched the sneakers up a little bit, not sure if the box was always this small but the 90s are and will always be one of my favorites.",
                        date = "December 10, 2016",
                        imageUrls = listOf(
                            R.drawable.img_product_shoes_bottom,
                            R.drawable.img_product_shoes_side,
                            R.drawable.img_product_shoes_back
                        )
                    )
                )
            }
        }
    }
}