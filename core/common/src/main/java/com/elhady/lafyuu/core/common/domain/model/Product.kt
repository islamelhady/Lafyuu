package com.elhady.lafyuu.core.common.domain.model

data class Product(
    val id: String,
    val name: String = "",
    val price: Double = 0.0,
    val originalPrice: Double? = null,
    val discountPercentage: Int = 0,
    val rating: Int = 0,
    val imageUrl: String? = null
)
