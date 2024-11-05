package com.elhady.lafyuu.feature.review.domain.usecase

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.feature.review.domain.repository.ReviewRepository
import javax.inject.Inject

class CreateReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(productId: String, rating: Int, comment: String): AppResult<Unit> {
        return repository.createReview(productId, rating, comment.trim())
    }
}
