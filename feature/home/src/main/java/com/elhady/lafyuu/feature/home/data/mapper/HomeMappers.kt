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

fun ProductDto.toDomain() = Product(
    id = id.orEmpty(),
    name = name ?: title.orEmpty(),
    coverPictureUrl = coverPictureUrl ?: coverUrl ?: imageUrl ?: image ?: productPictures?.firstOrNull(),
    basePrice = price ?: 0.0,
    discountPercentage = (discountPercentage ?: 0.0).toInt().coerceIn(0, 100),
    rating = rating?.toFloat()
)

fun OfferDto.toDomain() = Offer(
    id = id.orEmpty(),
    name = name ?: title.orEmpty(),
    description = description,
    coverUrl = coverUrl ?: coverPictureUrl ?: imageUrl
)
