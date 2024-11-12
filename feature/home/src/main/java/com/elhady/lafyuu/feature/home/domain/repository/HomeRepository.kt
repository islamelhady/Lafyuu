package com.elhady.lafyuu.feature.home.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.model.ProductQuery

interface HomeRepository {
    suspend fun getCategories(): AppResult<List<Category>>
    suspend fun getOffers(): AppResult<List<Offer>>
    suspend fun getProducts(query: ProductQuery): AppResult<List<Product>>
}
