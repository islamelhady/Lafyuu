package com.elhady.lafyuu.feature.search.data.mapper

import com.elhady.lafyuu.feature.search.data.remote.model.CategoryItemDto
import com.elhady.lafyuu.feature.search.data.remote.model.PagedListOfProductDto
import com.elhady.lafyuu.feature.search.data.remote.model.ProductDto
import com.elhady.lafyuu.feature.search.domain.model.Category
import com.elhady.lafyuu.feature.search.domain.model.Product
import com.elhady.lafyuu.feature.search.domain.model.ProductSort
import com.elhady.lafyuu.feature.search.domain.model.ProductsPage

fun ProductDto.toDomain(): Product {
    val basePrice = price ?: 0.0
    val discount = discountPercentage?.toInt()?.coerceIn(0, 100) ?: 0
    return Product(
        id = id.orEmpty(),
        name = name.orEmpty(),
        imageUrl = coverPictureUrl?.takeIf { it.isNotBlank() }
            ?: productPictures?.firstOrNull { it.isNotBlank() },
        price = basePrice * (100 - discount) / 100.0,
        originalPrice = basePrice.takeIf { discount > 0 },
        discountPercentage = discount,
        rating = rating?.toInt() ?: 0
    )
}

fun PagedListOfProductDto.toDomain(requestedPage: Int) = ProductsPage(
    // items without an id would crash the grid (duplicate keys), so they are dropped
    items = items.orEmpty().filter { !it.id.isNullOrBlank() }.map { it.toDomain() },
    page = page ?: requestedPage,
    hasNextPage = hasNextPage ?: false
)

fun CategoryItemDto.toDomain() = Category(
    id = id.orEmpty(),
    name = name.orEmpty()
)

/** (sortBy, sortOrder) as the API expects them */
fun ProductSort.toApiParams(): Pair<String, String> = when (this) {
    ProductSort.PRICE_ASC -> "price" to "asc"
    ProductSort.PRICE_DESC -> "price" to "desc"
}