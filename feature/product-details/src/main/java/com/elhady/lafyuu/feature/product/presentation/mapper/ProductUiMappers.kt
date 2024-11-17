package com.elhady.lafyuu.feature.product.presentation.mapper

import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.model.ProductReview
import com.elhady.lafyuu.feature.product.domain.model.RecommendedProduct
import com.elhady.lafyuu.feature.product.presentation.model.ProductDetailsUi
import com.elhady.lafyuu.feature.product.presentation.model.RecommendedProductUi
import com.elhady.lafyuu.feature.product.presentation.model.ReviewUi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
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
    userPicture = userPicture?.takeIf { it.isNotBlank() },
    rating = rating,
    comment = comment,
    date = formatReviewDate(createdAt)
)


internal fun formatReviewDate(
    raw: String,
    locale: Locale = Locale.getDefault(),
    zone: ZoneId = ZoneId.systemDefault()
): String {
    if (raw.isBlank()) return ""
    val date = parseLocalDate(raw, zone) ?: return raw
    return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale))
}

private fun parseLocalDate(raw: String, zone: ZoneId): LocalDate? =
    runCatching { OffsetDateTime.parse(raw).atZoneSameInstant(zone).toLocalDate() }.getOrNull()
        ?: runCatching {
            LocalDateTime.parse(raw).atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDate()
        }.getOrNull()
        ?: runCatching { LocalDate.parse(raw) }.getOrNull()

private fun formatPrice(value: Double): String = String.format(Locale.US, "\$%.2f", value)