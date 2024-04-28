package com.elhady.lafyuu.feature.product.domain.model

data class ProductReview(
    val comment: String = "",
    val rating: Float = 0f,
    val createdAt: String = "",
    val userName: String = "",
    val userPicture: String? = null
)
