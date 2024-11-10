package com.elhady.lafyuu.feature.product.presentation.model

data class ProductDetailsUi(
    val id: String,
    val name: String,
    val pictureUrls: List<String>,
    val price: String,
    val originalPrice: String?,
    val discountPercentage: Int,
    val rating: Int,
    val color: String,
    val reviewsCount: Int,
    val productCode: String,
    val stock: Int,
    val description: String
)

data class ReviewUi(
    val userName: String,
    val rating: Int,
    val comment: String,
    val date: String
)

data class RecommendedProductUi(
    val id: String,
    val name: String,
    val price: String,
    val originalPrice: String?,
    val discountPercentage: Int,
    val rating: Int,
    val imageUrl: String?
)