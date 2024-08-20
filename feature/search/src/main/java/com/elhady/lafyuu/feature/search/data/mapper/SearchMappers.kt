package com.elhady.lafyuu.feature.search.data.mapper

import com.elhady.lafyuu.feature.search.data.remote.model.ProductDto
import com.elhady.lafyuu.feature.search.domain.model.Product

fun ProductDto.toDomain(): Product {
    val rawPrice = price ?: 0.0
    val discount = (discountPercentage ?: 0).toDouble()
    val (calcPrice, origPrice, label) = if (discount > 0) {
        val calculatedCurrentPrice = rawPrice * (1.0 - discount / 100.0)
        Triple(
            calculatedCurrentPrice,
            originalPrice ?: rawPrice,
            "${discount.toInt()}% OFF"
        )
    } else {
        Triple(rawPrice, originalPrice, null)
    }

    return Product(
        id = id.orEmpty(),
        name = name ?: title.orEmpty(),
        coverPictureUrl = coverPictureUrl ?: coverUrl ?: imageUrl ?: image ?: productPictures?.firstOrNull(),
        price = calcPrice,
        originalPrice = origPrice,
        discountPercentage = if (discount > 0) discount else null,
        discountLabel = label,
        rating = rating
    )
}
