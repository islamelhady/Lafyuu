package com.elhady.lafyuu.feature.home.presentation.mapper

import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.presentation.model.CategoryUiModel
import com.elhady.lafyuu.feature.home.presentation.model.OfferUiModel
import com.elhady.lafyuu.feature.home.presentation.model.ProductUiModel
import java.util.Locale
import kotlin.math.roundToInt

fun Product.toUiModel() = ProductUiModel(
    id = id,
    name = name,
    imageUrl = coverPictureUrl,
    price = formatPrice(finalPrice),
    originalPrice = if (hasDiscount) formatPrice(basePrice) else null,
    discountPercentage = discountPercentage.takeIf { it > 0 },
    rating = rating?.roundToInt()?.coerceIn(0, 5)
)

fun Category.toUiModel() = CategoryUiModel(id = id, name = name)

fun Offer.toUiModel() = OfferUiModel(
    id = id,
    title = name,
    subtitle = description,
    imageUrl = coverUrl
)

private fun formatPrice(amount: Double): String = String.format(Locale.US, "$%.2f", amount)