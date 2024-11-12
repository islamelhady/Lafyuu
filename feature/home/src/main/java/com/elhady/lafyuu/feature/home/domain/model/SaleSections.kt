package com.elhady.lafyuu.feature.home.domain.model

const val PRODUCTS_POOL_SIZE = 30
private const val FLASH_SALE_SIZE = 15

enum class SaleSection { Flash, Mega }

data class SaleSections(val flash: List<Product>, val mega: List<Product>)

fun List<Product>.toSaleSections(): SaleSections {
    val discounted = filter { it.hasDiscount }.sortedByDescending { it.discountPercentage }
    return SaleSections(
        flash = discounted.take(FLASH_SALE_SIZE),
        mega = discounted.drop(FLASH_SALE_SIZE)
    )
}