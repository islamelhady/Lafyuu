package com.elhady.lafyuu.feature.product.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ProductDto(
    val id: String? = null,
    val productCode: String? = null,
    val name: String? = null,
    val title: String? = null,
    val description: String? = null,
    val arabicName: String? = null,
    val arabicDescription: String? = null,
    val coverPictureUrl: String? = null,
    val coverUrl: String? = null,
    val imageUrl: String? = null,
    val image: String? = null,
    val productPictures: List<String>? = null,
    val price: Double? = null,
    val originalPrice: Double? = null,
    val stock: Int? = null,
    val weight: Double? = null,
    val color: String? = null,
    val rating: Double? = null,
    val reviewsCount: Int? = null,
    val discountPercentage: Double? = null,
    val sellerId: String? = null,
    val categories: List<JsonElement>? = null
)

@Serializable
data class PagedListOfProductDto(
    val items: List<ProductDto>? = emptyList(),
    val products: List<ProductDto>? = emptyList(),
    val data: List<ProductDto>? = emptyList(),
    val page: Int? = 1,
    val pageSize: Int? = 10,
    val totalCount: Int? = 0,
    val totalPages: Int? = 0,
    val hasPreviousPage: Boolean? = false,
    val hasNextPage: Boolean? = false
) {
    val allProducts: List<ProductDto>
        get() = items?.ifEmpty { products?.ifEmpty { data.orEmpty() } ?: data.orEmpty() }
            ?: products?.ifEmpty { data.orEmpty() }
            ?: data.orEmpty()
}
