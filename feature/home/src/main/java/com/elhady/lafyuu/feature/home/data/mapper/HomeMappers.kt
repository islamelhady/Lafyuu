package com.elhady.lafyuu.feature.home.data.mapper

import com.elhady.lafyuu.feature.home.data.remote.model.CategoryDto
import com.elhady.lafyuu.feature.home.data.remote.model.OfferDto
import com.elhady.lafyuu.feature.home.data.remote.model.ProductDto
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product

fun CategoryDto.toDomain() = Category(
    id = id.orEmpty(),
    name = name ?: title.orEmpty(),
    description = description,
    coverPictureUrl = coverPictureUrl ?: coverUrl ?: iconUrl ?: image ?: imageUrl
)

fun ProductDto.toDomain(): Product {
    val rawPrice = price ?: 0.0
    val discount = discountPercentage ?: 0.0
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
        rating = rating?.toFloat()
    )
}

fun OfferDto.toDomain() = Offer(
    id = id.orEmpty(),
    name = name ?: title.orEmpty(),
    description = description,
    coverUrl = coverUrl ?: coverPictureUrl ?: imageUrl
)
