package com.elhady.lafyuu.feature.search.data.remote.model

import kotlinx.serialization.Serializable

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
    val price: Double? = 0.0,
    val originalPrice: Double? = null,
    val stock: Int? = 0,
    val weight: Double? = 0.0,
    val color: String? = null,
    val rating: Float? = null,
    val reviewsCount: Int? = 0,
    val discountPercentage: Int? = 0,
    val sellerId: String? = null,
    val categories: List<String>? = emptyList()
)

@Serializable
data class PagedListOfProductDto(
    val items: List<ProductDto>? = emptyList(),
    val page: Int? = 1,
    val pageSize: Int? = 20,
    val totalCount: Int? = 0,
    val hasNextPage: Boolean? = false,
    val hasPreviousPage: Boolean? = false
)
