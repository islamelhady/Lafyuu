package com.elhady.lafyuu.feature.home.domain.model

data class Product(
    val id: String,
    val name: String,
    val coverPictureUrl: String?,
    val price: Double,
    val originalPrice: Double?,
    val discountPercentage: Double?,
    val discountLabel: String?,
    val rating: Int?,
    val isFavorite: Boolean = false
)
