package com.elhady.lafyuu.core.common.domain.model

data class ProductReview(
    val comment: String,
    val rating: Int,
    val createdAt: String,
    val userName: String,
    val userPicture: String?
)
