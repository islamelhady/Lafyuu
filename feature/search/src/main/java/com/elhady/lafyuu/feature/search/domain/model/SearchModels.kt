package com.elhady.lafyuu.feature.search.domain.model

data class Product(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val price: Double = 0.0,
    val originalPrice: Double? = null,
    val discountPercentage: Int = 0,
    val rating: Int = 0
)

data class Category(
    val id: String,
    val name: String
)

data class ProductsPage(
    val items: List<Product>,
    val page: Int,
    val hasNextPage: Boolean
)

enum class ProductSort { PRICE_ASC, PRICE_DESC }

data class SearchFilters(
    val query: String,
    val category: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val inStockOnly: Boolean = false,
    val sort: ProductSort? = null
)