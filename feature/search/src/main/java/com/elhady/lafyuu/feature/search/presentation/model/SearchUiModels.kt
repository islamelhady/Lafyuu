package com.elhady.lafyuu.feature.search.presentation.model

data class ProductUi(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val price: String,
    val originalPrice: String?,
    val discountPercentage: Int,
    val rating: Int
)