package com.elhady.lafyuu.feature.product.domain.model

data class ProductReviewsInfo(
    val averageRating: Double,
    val reviewsCount: Int,
    val reviews: List<ProductReview>
)
