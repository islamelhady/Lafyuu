package com.elhady.lafyuu.feature.search.domain.model

data class Product(
    val id: String,
    val name: String,
    val coverPictureUrl: String?,
    val price: Double,
    val originalPrice: Double?,
    val discountPercentage: Double?,
    val discountLabel: String?,
    val rating: Float?
)
