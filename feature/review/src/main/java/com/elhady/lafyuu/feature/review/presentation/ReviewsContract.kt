package com.elhady.lafyuu.feature.review.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.review.domain.model.ProductReview

data class ReviewsUiState(
    val isLoading: Boolean = false,
    val productId: String = "",
    val averageRating: Double = 0.0,
    val reviewsCount: Int = 0,
    val reviews: List<ProductReview> = emptyList(),
    val selectedRatingFilter: Int = 0,
    val isWriteReviewDialogVisible: Boolean = false,
    val newReviewRating: Int = 5,
    val newReviewComment: String = "",
    val isSubmittingReview: Boolean = false,
    val error: String? = null
)

sealed interface ReviewsUiEvent {
    data class LoadReviews(val productId: String) : ReviewsUiEvent
    data class FilterRating(val rating: Int) : ReviewsUiEvent
    data object OpenWriteReviewDialog : ReviewsUiEvent
    data object CloseWriteReviewDialog : ReviewsUiEvent
    data class UpdateNewReviewRating(val rating: Int) : ReviewsUiEvent
    data class UpdateNewReviewComment(val comment: String) : ReviewsUiEvent
    data object SubmitReview : ReviewsUiEvent
    data object BackClicked : ReviewsUiEvent
    data object RetryClicked : ReviewsUiEvent
}

sealed interface ReviewsUiEffect {
    data object NavigateBack : ReviewsUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : ReviewsUiEffect
}
