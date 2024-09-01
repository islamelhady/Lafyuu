package com.elhady.lafyuu.feature.review.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.review.domain.model.ProductReviewsInfo
import com.elhady.lafyuu.feature.review.domain.repository.ReviewRepository
import javax.inject.Inject

class GetProductReviewsInfoUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(productId: String, page: Int = 1, pageSize: Int = 50): AppResult<ProductReviewsInfo> {
        if (productId.isBlank()) {
            return AppResult.Error("Invalid product ID")
        }
        return repository.getProductReviewsInfo(productId, page, pageSize)
    }
}
