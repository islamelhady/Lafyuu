package com.elhady.lafyuu.feature.review.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.review.domain.model.ProductReviewsInfo

interface ReviewRepository {
    suspend fun getProductReviewsInfo(productId: String, page: Int = 1, pageSize: Int = 50): AppResult<ProductReviewsInfo>
    suspend fun createReview(productId: String, rating: Int, comment: String): AppResult<Unit>
}
