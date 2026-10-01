package com.elhady.lafyuu.feature.product.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UserReviewDto(
    val comment: String? = null,
    val rating: Int? = null,
    val createdAt: String? = null,
    val userName: String? = null,
    val userPicture: String? = null
)

@Serializable
data class PagedListOfUserReviewDto(
    val items: List<UserReviewDto>? = emptyList(),
    val page: Int? = 1,
    val pageSize: Int? = 10,
    val totalCount: Int? = 0,
    val totalPages: Int? = 0
)

@Serializable
data class GetProductReviewsResponseDto(
    val message: String? = null,
    val averageRating: Double? = null,
    val reviewsCount: Int? = null,
    val reviews: PagedListOfUserReviewDto? = null
)
