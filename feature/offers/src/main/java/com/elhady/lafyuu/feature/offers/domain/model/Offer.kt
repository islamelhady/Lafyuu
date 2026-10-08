package com.elhady.lafyuu.feature.offers.domain.model

data class Offer(
    val id: String,
    val name: String,
    val description: String?,
    val coverUrl: String?
)
