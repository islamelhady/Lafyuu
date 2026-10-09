package com.elhady.lafyuu.feature.explore.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryItemDto(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val coverPictureUrl: String? = null,
    val coverUrl: String? = null,
    val iconUrl: String? = null,
    val image: String? = null,
    val imageUrl: String? = null
)

@Serializable
data class GetAllCategoriesResponseDto(
    val categories: List<CategoryItemDto>? = emptyList(),
    val items: List<CategoryItemDto>? = emptyList(),
    val data: List<CategoryItemDto>? = emptyList()
) {
    val allCategories: List<CategoryItemDto>
        get() = categories?.ifEmpty { items?.ifEmpty { data.orEmpty() } ?: data.orEmpty() }
            ?: items?.ifEmpty { data.orEmpty() }
            ?: data.orEmpty()
}
