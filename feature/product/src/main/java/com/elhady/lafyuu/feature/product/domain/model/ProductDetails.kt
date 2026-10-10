package com.elhady.lafyuu.feature.product.domain.model

data class ProductDetails(
    val id: String,
    val productCode: String = "",
    val name: String = "",
    val description: String = "",
    val coverPictureUrl: String = "",
    val productPictures: List<String> = emptyList(),
    val price: Double = 0.0,
    val originalPrice: Double? = null,
    val discountPercentage: Int = 0,
    val stock: Int = 0,
    val color: String = "",
    val availableColorsHex: List<String> = emptyList(),
    val availableSizes: List<String> = emptyList(),
    val rating: Int = 0,
    val reviewsCount: Int = 0,
    val sellerId: String = ""
)
