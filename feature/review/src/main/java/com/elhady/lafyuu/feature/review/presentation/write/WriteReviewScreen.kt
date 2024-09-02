package com.elhady.lafyuu.feature.review.presentation.write

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.other.RatingBar
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.TextAreaField
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun WriteReviewRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WriteReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                WriteReviewUiEffect.NavigateBack -> onNavigateBack()
                is WriteReviewUiEffect.ShowSnackbar -> {
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

    WriteReviewScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun WriteReviewScreen(
    uiState: WriteReviewUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (WriteReviewUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Write Review",
                leadingIcon = Left,
                onLeadingClick = { onEvent(WriteReviewUiEvent.BackClicked) }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Theme.space.large)
            ) {
                DefaultButton(
                    caption = "Submit",
                    isLoading = uiState.isLoading,
                    onClick = { onEvent(WriteReviewUiEvent.SubmitClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(Theme.space.large)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Theme.space.large)
        ) {
            LafyuuText(
                text = "Please write Overall level of satisfaction with your shipping / Delivery service",
                style = Theme.typography.heading5,
                color = Theme.color.neutralDark
            )
            RatingBar(
                rating = uiState.rating,
                iconSize = Theme.size.iconLarge,
                showRatingText = true,
                onRatingChanged = { newRating ->
                    onEvent(WriteReviewUiEvent.RatingChanged(newRating.toInt()))
                }
            )
            LafyuuText(
                text = "Write Your Review",
                style = Theme.typography.heading5,
                color = Theme.color.neutralDark
            )
            TextAreaField(
                value = uiState.comment,
                onValueChange = { onEvent(WriteReviewUiEvent.CommentChanged(it)) },
                placeholder = "Write your review here",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
        }
    }
}
