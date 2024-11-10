package com.elhady.lafyuu.feature.product.presentation.mapper

import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.model.ProductReview
import com.elhady.lafyuu.feature.product.domain.model.RecommendedProduct
import com.elhady.lafyuu.feature.product.presentation.model.ProductDetailsUi
import com.elhady.lafyuu.feature.product.presentation.model.RecommendedProductUi
import com.elhady.lafyuu.feature.product.presentation.model.ReviewUi
import java.util.Locale

fun ProductDetails.toUi() = ProductDetailsUi(
    id = id,
    name = name,
    pictureUrls = pictureUrls,
    price = formatPrice(finalPrice),
    originalPrice = if (discountPercentage > 0) formatPrice(basePrice) else null,
    discountPercentage = discountPercentage,
    rating = rating,
    color = color,
    reviewsCount = reviewsCount,
    productCode = productCode,
    stock = stock,
    description = description
)

fun RecommendedProduct.toUi() = RecommendedProductUi(
    id = id,
    name = name,
    price = formatPrice(finalPrice),
    originalPrice = if (discountPercentage > 0) formatPrice(basePrice) else null,
    discountPercentage = discountPercentage,
    rating = rating,
    imageUrl = imageUrl
)

fun ProductReview.toUi() = ReviewUi(
    userName = userName,
    rating = rating,
    comment = comment,
    date = createdAt
)

private fun formatPrice(value: Double): String = String.format(Locale.US, "\$%.2f", value)