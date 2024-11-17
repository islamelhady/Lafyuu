package com.elhady.lafyuu.feature.search.presentation.mapper

import com.elhady.lafyuu.feature.search.domain.model.Product
import com.elhady.lafyuu.feature.search.domain.model.ProductSort
import com.elhady.lafyuu.feature.search.domain.model.SearchFilters
import com.elhady.lafyuu.feature.search.presentation.FilterDraft
import com.elhady.lafyuu.feature.search.presentation.model.ProductUi
import java.util.Locale

fun Product.toUi() = ProductUi(
    id = id,
    name = name,
    imageUrl = imageUrl,
    price = formatPrice(price),
    originalPrice = originalPrice?.let(::formatPrice),
    discountPercentage = discountPercentage,
    rating = rating
)

fun FilterDraft.toDomain(query: String, sort: ProductSort?): SearchFilters =
    SearchFilters(
        query = query.trim(),
        category = category,
        minPrice = minPrice.takeIf { isPriceFilterEnabled }?.toDouble(),
        maxPrice = maxPrice.takeIf { isPriceFilterEnabled }?.toDouble(),
        inStockOnly = inStockOnly,
        sort = sort
    )

private fun formatPrice(value: Double): String = String.format(Locale.US, "\$%.2f", value)
