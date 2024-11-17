package com.elhady.lafyuu.feature.search.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: String? = null,
    val name: String? = null,
    val coverPictureUrl: String? = null,
    val productPictures: List<String>? = null,
    val price: Double? = null,
    val rating: Double? = null,
    val discountPercentage: Double? = null
)

@Serializable
data class PagedListOfProductDto(
    val items: List<ProductDto>? = emptyList(),
    val page: Int? = null,
    val pageSize: Int? = null,
    val totalCount: Int? = null,
    val hasNextPage: Boolean? = false,
    val hasPreviousPage: Boolean? = false
)

@Serializable
data class CategoryItemDto(
    val id: String? = null,
    val name: String? = null
)

@Serializable
data class GetAllCategoriesResponseDto(
    val categories: List<CategoryItemDto>? = emptyList()
)