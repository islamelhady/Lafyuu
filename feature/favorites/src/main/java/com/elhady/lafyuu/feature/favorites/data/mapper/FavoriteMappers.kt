package com.elhady.lafyuu.feature.favorites.data.mapper

import com.elhady.lafyuu.core.database.model.Favorite
import com.elhady.lafyuu.feature.favorites.domain.model.FavoriteProduct

fun Favorite.toDomain() = FavoriteProduct(
    id = productId,
    name = name,
    price = price,
    originalPrice = originalPrice,
    discountPercentage = discountPercentage,
    rating = rating,
    imageUrl = imageUrl
)