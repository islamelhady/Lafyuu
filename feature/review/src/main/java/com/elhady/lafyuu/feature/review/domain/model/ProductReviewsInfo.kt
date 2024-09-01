package com.elhady.lafyuu.feature.review.domain.model

data class ProductReviewsInfo(
    val averageRating: Double,
    val reviewsCount: Int,
    val reviews: List<ProductReview>
)
