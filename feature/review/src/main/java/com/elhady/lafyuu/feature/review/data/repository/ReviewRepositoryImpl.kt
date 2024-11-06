package com.elhady.lafyuu.feature.review.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.review.data.mapper.toDomain
import com.elhady.lafyuu.feature.review.data.remote.ReviewApi
import com.elhady.lafyuu.feature.review.data.remote.model.CreateReviewRequestDto
import com.elhady.lafyuu.feature.review.domain.model.ProductReviewsInfo
import com.elhady.lafyuu.feature.review.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi,
    private val safeApiCaller: SafeApiCaller
) : ReviewRepository {

    override suspend fun getProductReviewsInfo(
        productId: String,
        page: Int,
        pageSize: Int
    ): AppResult<ProductReviewsInfo> {
        return safeApiCaller.call {
            reviewApi.getProductReviews(productId, page, pageSize).toDomain()
        }
    }

    override suspend fun createReview(
        productId: String,
        rating: Int,
        comment: String
    ): AppResult<Unit> {
        return safeApiCaller.call {
            reviewApi.createReview(
                productId,
                CreateReviewRequestDto(rating = rating, comment = comment)
            )
        }
    }
}
