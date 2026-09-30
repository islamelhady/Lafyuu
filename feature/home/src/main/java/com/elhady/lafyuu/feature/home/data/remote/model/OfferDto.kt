package com.elhady.lafyuu.feature.home.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class OfferDto(
    val id: String? = null,
    val name: String? = null,
    val title: String? = null,
    val description: String? = null,
    val coverUrl: String? = null,
    val coverPictureUrl: String? = null,
    val imageUrl: String? = null,
    val createdAt: String? = null
)

@Serializable
data class GetAllOffersResponse(
    val items: List<OfferDto>? = emptyList(),
    val offers: List<OfferDto>? = emptyList(),
    val data: List<OfferDto>? = emptyList(),
    val page: Int? = 1,
    val pageSize: Int? = 10,
    val totalCount: Int? = 0,
    val totalPages: Int? = 0,
    val hasPreviousPage: Boolean? = false,
    val hasNextPage: Boolean? = false
) {
    val allOffers: List<OfferDto>
        get() = items?.ifEmpty { offers?.ifEmpty { data.orEmpty() } ?: data.orEmpty() }
            ?: offers?.ifEmpty { data.orEmpty() }
            ?: data.orEmpty()
}
