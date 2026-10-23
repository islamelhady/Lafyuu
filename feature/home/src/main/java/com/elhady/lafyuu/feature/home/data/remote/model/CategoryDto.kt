package com.elhady.lafyuu.feature.home.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: String? = null,
    val name: String? = null,
    val title: String? = null,
    val description: String? = null,
    val coverPictureUrl: String? = null,
    val coverUrl: String? = null,
    val iconUrl: String? = null,
    val image: String? = null,
    val imageUrl: String? = null
)

@Serializable
data class GetAllCategoriesResponse(
    val categories: List<CategoryDto>? = emptyList(),
    val items: List<CategoryDto>? = emptyList(),
    val data: List<CategoryDto>? = emptyList()
) {
    val allCategories: List<CategoryDto>
        get() = categories?.ifEmpty { items?.ifEmpty { data.orEmpty() } ?: data.orEmpty() }
            ?: items?.ifEmpty { data.orEmpty() }
            ?: data.orEmpty()
}
