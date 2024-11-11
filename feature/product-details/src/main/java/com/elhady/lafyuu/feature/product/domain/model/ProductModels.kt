package com.elhady.lafyuu.feature.product.domain.model

data class ProductDetails(
    val id: String,
    val productCode: String,
    val name: String,
    val description: String,
    val pictureUrls: List<String>,
    val basePrice: Double,
    val finalPrice: Double,
    val discountPercentage: Int,
    val stock: Int,
    val color: String,
    val rating: Int,
    val reviewsCount: Int,
    val sellerId: String
)

data class RecommendedProduct(
    val id: String,
    val name: String,
    val basePrice: Double,
    val finalPrice: Double,
    val discountPercentage: Int,
    val rating: Int,
    val imageUrl: String?
)

data class ProductReview(
    val comment: String,
    val rating: Int,
    val createdAt: String,
    val userName: String,
    val userPicture: String?
)

data class ProductContent(
    val details: ProductDetails,
    val reviews: List<ProductReview>,
    val recommendedProducts: List<RecommendedProduct>
)