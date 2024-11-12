package com.elhady.lafyuu.feature.favorites.presentation.mapper

import com.elhady.lafyuu.feature.favorites.domain.model.FavoriteProduct
import com.elhady.lafyuu.feature.favorites.presentation.model.FavoriteProductUi
import java.util.Locale

fun FavoriteProduct.toUi() = FavoriteProductUi(
    id = id,
    name = name,
    price = formatPrice(price),
    originalPrice = originalPrice?.let(::formatPrice),
    discountPercentage = discountPercentage,
    rating = rating,
    imageUrl = imageUrl
)

private fun formatPrice(value: Double): String = String.format(Locale.US, "\$%.2f", value)