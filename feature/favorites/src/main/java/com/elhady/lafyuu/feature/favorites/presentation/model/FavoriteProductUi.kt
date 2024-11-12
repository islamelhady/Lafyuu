package com.elhady.lafyuu.feature.favorites.presentation.model

data class FavoriteProductUi(
    val id: String,
    val name: String,
    val price: String,
    val originalPrice: String?,
    val discountPercentage: Int,
    val rating: Int,
    val imageUrl: String?
)