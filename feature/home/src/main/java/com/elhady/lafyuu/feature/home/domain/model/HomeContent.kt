package com.elhady.lafyuu.feature.home.domain.model

data class HomeContent(
    val categories: List<Category> = emptyList(),
    val offers: List<Offer> = emptyList(),
    val flashSaleProducts: List<Product> = emptyList(),
    val megaSaleProducts: List<Product> = emptyList(),
    val recommendedProducts: List<Product> = emptyList()
)
