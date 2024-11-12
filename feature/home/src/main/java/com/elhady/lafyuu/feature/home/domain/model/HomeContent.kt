package com.elhady.lafyuu.feature.home.domain.model

enum class HomeSection { Offers, Categories, FlashSale, MegaSale, Recommended }
data class HomeContent(
    val categories: List<Category> = emptyList(),
    val offers: List<Offer> = emptyList(),
    val flashSaleProducts: List<Product> = emptyList(),
    val megaSaleProducts: List<Product> = emptyList(),
    val recommendedProducts: List<Product> = emptyList(),
    val failedSections: Set<HomeSection> = emptySet()
)

data class Product(
    val id: String,
    val name: String,
    val coverPictureUrl: String?,
    val basePrice: Double,
    val discountPercentage: Int,
    val rating: Float?
) {
    val hasDiscount: Boolean get() = discountPercentage > 0
    val finalPrice: Double get() = if (hasDiscount) basePrice * (1.0 - discountPercentage / 100.0) else basePrice
}

enum class SortField { Price, Name, Rating, Discount }
enum class SortOrder { Ascending, Descending }

data class ProductQuery(
    val searchTerm: String? = null,
    val category: String? = null,
    val sortBy: SortField? = null,
    val sortOrder: SortOrder = SortOrder.Ascending,
    val page: Int = 1,
    val pageSize: Int = 20
)
