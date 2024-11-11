package com.elhady.lafyuu.feature.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.elhady.lafyuu.core.designsystem.components.card.InformationCard
import com.elhady.lafyuu.core.designsystem.components.element.ReviewCard
import com.elhady.lafyuu.core.designsystem.components.other.RatingBar
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.R
import com.elhady.lafyuu.core.designsystem.R as DsR
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiIntent
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiState

private const val REVIEWS_PREVIEW_COUNT = 2
@Composable
fun ReviewsSection(
    uiState: ProductDetailsUiState,
    onIntent: (ProductDetailsUiIntent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
        if (uiState.reviews.isNotEmpty() && uiState.product != null) {
            SectionTitle(
                title = stringResource(R.string.product_reviews_title),
                actionText = stringResource(R.string.product_see_more),
                onActionClick = { onIntent(ProductDetailsUiIntent.SeeMoreReviewsClicked) }
            )
            RatingBar(
                rating = uiState.product.rating,
                iconSize = Theme.size.iconSmall,
                showRatingText = true,
                showReviewNumber = uiState.product.reviewsCount.toString()
            )
            uiState.reviews.take(REVIEWS_PREVIEW_COUNT).forEach { review ->
                ReviewCard(
                    userName = review.userName,
                    avatarUrl = DsR.drawable.img_profile_boy,
                    avatarImageUrl = review.userPicture,
                    rating = review.rating,
                    comment = review.comment,
                    date = review.date
                )
            }
            WriteReviewButton(R.string.product_write_review, onIntent)
        } else {
            SectionTitle(title = stringResource(R.string.product_reviews_title))
            InformationCard(information = stringResource(R.string.product_no_reviews))
            WriteReviewButton(R.string.product_write_first_review, onIntent)
        }
    }
}