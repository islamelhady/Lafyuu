package com.elhady.lafyuu.feature.review.data.mapper

import com.elhady.lafyuu.feature.review.data.remote.model.GetProductReviewsResponseDto
import com.elhady.lafyuu.feature.review.data.remote.model.UserReviewDto
import com.elhady.lafyuu.feature.review.domain.model.ProductReviewsInfo
import com.elhady.lafyuu.feature.review.domain.model.ProductReview

fun UserReviewDto.toDomain(): ProductReview {
    return ProductReview(
        comment = comment.orEmpty(),
        rating = rating ?: 0,
        createdAt = createdAt.orEmpty(),
        userName = userName.orEmpty(),
        userPicture = userPicture
    )
}

fun GetProductReviewsResponseDto.toDomain(): ProductReviewsInfo {
    return ProductReviewsInfo(
        averageRating = averageRating ?: 0.0,
        reviewsCount = reviewsCount ?: 0,
        reviews = reviews?.items?.map{it.toDomain()}.orEmpty()
    )
}