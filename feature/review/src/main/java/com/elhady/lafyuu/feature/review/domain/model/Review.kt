package com.elhady.lafyuu.feature.review.domain.model

data class ProductReview(
    val comment: String,
    val rating: Int,
    val createdAt: String,
    val userName: String,
    val userPicture: String?
)

data class ProductReviewsInfo(
    val averageRating: Double,
    val reviewsCount: Int,
    val reviews: List<ProductReview>
)