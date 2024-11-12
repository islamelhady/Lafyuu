package com.elhady.lafyuu.feature.favorites.domain.model

data class FavoriteProduct(
    val id: String,
    val name: String,
    val price: Double,
    val originalPrice: Double?,
    val discountPercentage: Int,
    val rating: Int,
    val imageUrl: String?
)