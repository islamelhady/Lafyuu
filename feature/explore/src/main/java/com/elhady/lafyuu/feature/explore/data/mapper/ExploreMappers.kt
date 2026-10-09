package com.elhady.lafyuu.feature.explore.data.mapper

import com.elhady.lafyuu.feature.explore.data.remote.model.CategoryItemDto
import com.elhady.lafyuu.feature.explore.domain.model.Category

fun CategoryItemDto.toDomain() = Category(
    id = id.orEmpty(),
    name = name.orEmpty(),
    description = description.orEmpty(),
    coverPictureUrl = coverPictureUrl ?: coverUrl ?: iconUrl ?: image ?: imageUrl
)
