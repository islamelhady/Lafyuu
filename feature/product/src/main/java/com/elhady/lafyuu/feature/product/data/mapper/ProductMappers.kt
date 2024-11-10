package com.elhady.lafyuu.feature.product.data.mapper

import com.elhady.lafyuu.feature.product.data.remote.model.ProductDto
import com.elhady.lafyuu.feature.product.data.remote.model.UserReviewDto
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.model.ProductReview
import com.elhady.lafyuu.feature.product.domain.model.RecommendedProduct
import kotlin.math.roundToInt

fun ProductDto.toProductDetails(): ProductDetails {
    val discount = discountPercentage.toValidDiscount()
    val basePrice = price ?: 0.0
    return ProductDetails(
        id = id.orEmpty(),
        productCode = productCode.orEmpty(),
        name = name.orEmpty(),
        description = description ?: arabicDescription.orEmpty(),
        pictureUrls = (listOfNotNull(coverPictureUrl) + productPictures.orEmpty())
            .filter { it.isNotBlank() }
            .distinct(),
        basePrice = basePrice,
        finalPrice = applyDiscount(basePrice, discount),
        discountPercentage = discount,
        stock = stock ?: 0,
        color = color.orEmpty(),
        rating = rating?.roundToInt() ?: 0,
        reviewsCount = reviewsCount ?: 0,
        sellerId = sellerId.orEmpty()
    )
}

fun ProductDto.toRecommendedProduct(): RecommendedProduct {
    val discount = discountPercentage.toValidDiscount()
    val basePrice = price ?: 0.0
    return RecommendedProduct(
        id = id.orEmpty(),
        name = name.orEmpty(),
        basePrice = basePrice,
        finalPrice = applyDiscount(basePrice, discount),
        discountPercentage = discount,
        rating = rating?.roundToInt() ?: 0,
        imageUrl = coverPictureUrl ?: productPictures?.firstOrNull()
    )
}

fun UserReviewDto.toReview(): ProductReview = ProductReview(
    comment = comment.orEmpty(),
    rating = rating ?: 0,
    createdAt = createdAt.orEmpty(),
    userName = userName.orEmpty(),
    userPicture = userPicture
)

private fun Double?.toValidDiscount(): Int = this?.roundToInt()?.coerceIn(0, 100) ?: 0

private fun applyDiscount(basePrice: Double, discount: Int): Double = basePrice * (100 - discount) / 100.0
