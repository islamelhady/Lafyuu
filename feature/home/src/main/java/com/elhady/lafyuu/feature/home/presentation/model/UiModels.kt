package com.elhady.lafyuu.feature.home.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class OfferUiModel(
    val id: String,
    val title: String,
    val subtitle: String?,
    val imageUrl: String?
)

@Immutable
data class CategoryUiModel(val id: String, val name: String)

@Immutable
data class ProductUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val price: String,
    val originalPrice: String?,
    val discountPercentage: Int?,
    val rating: Int?
)