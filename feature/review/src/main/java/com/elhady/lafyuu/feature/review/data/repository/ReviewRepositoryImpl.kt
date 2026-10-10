package com.elhady.lafyuu.feature.review.data.repository

import android.util.Log
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.review.data.mapper.toDomain
import com.elhady.lafyuu.feature.review.data.remote.ReviewApi
import com.elhady.lafyuu.feature.review.data.remote.model.CreateReviewRequestDto
import com.elhady.lafyuu.feature.review.domain.model.ProductReviewsInfo
import com.elhady.lafyuu.feature.review.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi,
    private val apiErrorParser: ApiErrorParser
) : ReviewRepository {

    override suspend fun getProductReviewsInfo(
        productId: String,
        page: Int,
        pageSize: Int
    ): AppResult<ProductReviewsInfo> {
        return try {
            val response = reviewApi.getProductReviews(
                productId = productId,
                page = page,
                pageSize = pageSize
            )
            if (response.isSuccessful) {
                val body = response.body()
                val avg = body?.averageRating ?: 0.0
                val count = body?.reviewsCount ?: 0
                val reviews = body?.reviews?.items.orEmpty().map { it.toDomain() }
                AppResult.Success(ProductReviewsInfo(avg, count, reviews))
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (ex: Exception) {
            Log.e("ReviewRepository", "getProductReviewsInfo failure: ${ex.message}", ex)
            mapError(apiErrorParser.parseException(ex))
        }
    }

    override suspend fun createReview(
        productId: String,
        rating: Int,
        comment: String
    ): AppResult<Unit> {
        return try {
            val response = reviewApi.createReview(
                productId = productId,
                request = CreateReviewRequestDto(rating = rating, comment = comment)
            )
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (ex: Exception) {
            Log.e("ReviewRepository", "createReview failure: ${ex.message}", ex)
            mapError(apiErrorParser.parseException(ex))
        }
    }

    private fun mapError(networkError: NetworkError): AppResult.Error {
        return when (networkError) {
            is NetworkError.ApiError -> {
                val details = networkError.details
                val errorMessage = details.detail
                    ?: details.title
                    ?: details.errors?.values?.flatten()?.firstOrNull()
                    ?: "Request failed"
                AppResult.Error(message = errorMessage)
            }
            is NetworkError.Connectivity -> AppResult.Error(message = "No internet connection")
            is NetworkError.Serialization -> AppResult.Error(message = "Server error (Parsing)")
            is NetworkError.Server -> AppResult.Error(message = "Internal server error")
            is NetworkError.Unknown -> AppResult.Error(
                message = networkError.throwable.localizedMessage ?: "An unexpected error occurred",
                throwable = networkError.throwable
            )
        }
    }
}
