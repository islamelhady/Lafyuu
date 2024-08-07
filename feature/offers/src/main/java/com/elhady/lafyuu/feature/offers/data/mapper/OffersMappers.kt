package com.elhady.lafyuu.feature.offers.data.mapper

import com.elhady.lafyuu.feature.offers.data.remote.model.OfferDto
import com.elhady.lafyuu.feature.offers.domain.model.Offer

fun OfferDto.toDomain() = Offer(
    id = id.orEmpty(),
    name = name ?: title.orEmpty(),
    description = description,
    coverUrl = coverUrl ?: coverPictureUrl ?: imageUrl
)
