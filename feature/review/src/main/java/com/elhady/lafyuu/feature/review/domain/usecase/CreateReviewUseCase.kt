package com.elhady.lafyuu.feature.review.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.review.domain.repository.ReviewRepository
import javax.inject.Inject

class CreateReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(productId: String, rating: Int, comment: String): AppResult<Unit> {
        if (productId.isBlank()) {
            return AppResult.Error("Invalid product ID")
        }
        if (rating < 1 || rating > 5) {
            return AppResult.Error("Rating must be between 1 and 5")
        }
        if (comment.isBlank()) {
            return AppResult.Error("Comment cannot be empty")
        }
        return repository.createReview(productId, rating, comment.trim())
    }
}
