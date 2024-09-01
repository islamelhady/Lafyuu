package com.elhady.lafyuu.feature.review.data.mapper

import com.elhady.lafyuu.feature.review.data.remote.model.UserReviewDto
import com.elhady.lafyuu.feature.review.domain.model.ProductReview

fun UserReviewDto.toDomain(): ProductReview {
    return ProductReview(
        comment = comment.orEmpty(),
        rating = rating?.toFloat() ?: 0f,
        createdAt = createdAt.orEmpty(),
        userName = userName.orEmpty(),
        userPicture = userPicture
    )
}
