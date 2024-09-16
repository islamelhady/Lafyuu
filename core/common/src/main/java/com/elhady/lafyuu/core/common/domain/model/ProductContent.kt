package com.elhady.lafyuu.core.common.domain.model

data class ProductContent(
    val details: ProductDetails,
    val reviews: List<ProductReview> = emptyList(),
    val recommendedProducts: List<Product> = emptyList()
)
