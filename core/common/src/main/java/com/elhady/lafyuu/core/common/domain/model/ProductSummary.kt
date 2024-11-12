package com.elhady.lafyuu.core.common.domain.model

data class ProductSummary(
    val id: String,
    val name: String,
    val coverPictureUrl: String?,
    val productPictures: List<String> = emptyList(),
    val price: Double,
    val originalPrice: Double?,
    val discountPercentage: Int?,
    val rating: Int?
)
