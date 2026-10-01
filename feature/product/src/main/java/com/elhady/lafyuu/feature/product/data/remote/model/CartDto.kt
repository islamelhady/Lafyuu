package com.elhady.lafyuu.feature.product.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AddItemToCartRequestDto(
    val productId: String,
    val quantity: Int
)

@Serializable
data class AddItemToCartResponseDto(
    val message: String? = null,
    val id: String? = null,
    val productId: String? = null,
    val quantity: Int? = null
)
