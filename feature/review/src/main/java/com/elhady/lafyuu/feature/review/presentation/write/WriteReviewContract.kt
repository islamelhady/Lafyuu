package com.elhady.lafyuu.feature.review.presentation.write

import com.elhady.lafyuu.core.designsystem.components.element.AlertType

data class WriteReviewUiState(
    val productId: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface WriteReviewUiEvent {
    data class RatingChanged(val rating: Int) : WriteReviewUiEvent
    data class CommentChanged(val comment: String) : WriteReviewUiEvent
    data object SubmitClicked : WriteReviewUiEvent
    data object BackClicked : WriteReviewUiEvent
}

sealed interface WriteReviewUiEffect {
    data object NavigateBack : WriteReviewUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : WriteReviewUiEffect
}
