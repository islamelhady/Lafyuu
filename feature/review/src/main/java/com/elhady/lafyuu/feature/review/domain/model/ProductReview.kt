package com.elhady.lafyuu.feature.review.domain.model

data class ProductReview(
    val comment: String,
    val rating: Float,
    val createdAt: String,
    val userName: String,
    val userPicture: String?
)
