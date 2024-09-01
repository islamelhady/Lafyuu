package com.elhady.lafyuu.feature.review.presentation

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.FilterReviewBar
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.ReviewCard
import com.elhady.lafyuu.core.designsystem.components.element.ReviewData
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.other.RatingBar
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ReviewsRoute(
    onNavigateBack: () -> Unit,
    onNavigateToWriteReview: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ReviewsUiEffect.NavigateBack -> onNavigateBack()
                is ReviewsUiEffect.NavigateToWriteReview -> onNavigateToWriteReview(effect.productId)
                is ReviewsUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        visuals = LafyuuSnackBarVisuals(
                            message = effect.message,
                            type = effect.type
                        )
                    )
                }
            }
        }
    }

    ReviewsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun ReviewsScreen(
    uiState: ReviewsUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (ReviewsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredReviews = if (uiState.selectedRatingFilter == 0) {
        uiState.reviews
    } else {
        uiState.reviews.filter { it.rating.toInt() == uiState.selectedRatingFilter }
    }

    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "${uiState.reviewsCount} Reviews",
                leadingIcon = Left,
                onLeadingClick = { onEvent(ReviewsUiEvent.BackClicked) }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Theme.space.large)
            ) {
                DefaultButton(
                    caption = "Write Review",
                    onClick = { onEvent(ReviewsUiEvent.WriteReviewClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(Theme.space.large)
        ) {
            when {
                uiState.isLoading && uiState.reviews.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Theme.color.blue
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(Theme.space.large)
                    ) {
                        // Summary Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
                        ) {
                            RatingBar(
                                rating = uiState.averageRating.toFloat(),
                                iconSize = Theme.size.iconMedium
                            )
                            LafyuuText(
                                text = "%.1f".format(uiState.averageRating),
                                style = Theme.typography.heading3,
                                color = Theme.color.neutralDark
                            )
                            LafyuuText(
                                text = "(${uiState.reviewsCount} Reviews)",
                                style = Theme.typography.normalTextRegular,
                                color = Theme.color.neutralGrey
                            )
                        }

                        // Rating Filter Bar
                        FilterReviewBar(
                            selectedStars = uiState.selectedRatingFilter,
                            onFilterSelected = { onEvent(ReviewsUiEvent.FilterRating(it)) }
                        )

                        // Reviews List
                        if (filteredReviews.isEmpty()) {
                            InfoStateContent(
                                errorMessage = "No reviews found for this filter",
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = Theme.space.large),
                                verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                            ) {
                                items(filteredReviews, key = { it.userName + it.createdAt }) { review ->
                                    ReviewCard(
                                        review = ReviewData(
                                            userName = review.userName,
                                            avatarUrl = R.drawable.img_profile_man_bearded,
                                            rating = review.rating,
                                            comment = review.comment,
                                            date = review.createdAt
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
