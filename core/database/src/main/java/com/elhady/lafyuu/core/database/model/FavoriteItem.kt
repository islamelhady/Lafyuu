package com.elhady.lafyuu.core.database.model

data class Favorite(
    val productId: String,
    val name: String,
    val imageUrl: String?,
    val price: Double,
    val originalPrice: Double?,
    val discountPercentage: Int,
    val rating: Int
)