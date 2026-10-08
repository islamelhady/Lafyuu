package com.elhady.lafyuu.feature.offers.data.remote.model

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
data class PaginatedOffersDto(
    val items: List<OfferDto>? = emptyList(),
    val page: Int? = 1,
    val pageSize: Int? = 10,
    val totalCount: Int? = 0,
    val totalPages: Int? = 0,
    val hasNextPage: Boolean? = false,
    val hasPreviousPage: Boolean? = false
)

@Serializable
data class GetAllOffersResponseDto(
    val offers: PaginatedOffersDto? = null,
    val items: List<OfferDto>? = emptyList(),
    val data: List<OfferDto>? = emptyList()
) {
    val allOffers: List<OfferDto>
        get() = offers?.items
            ?: items?.ifEmpty { data.orEmpty() }
            ?: data.orEmpty()
}
