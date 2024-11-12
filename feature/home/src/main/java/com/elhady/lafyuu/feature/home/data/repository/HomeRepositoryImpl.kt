package com.elhady.lafyuu.feature.home.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.home.data.mapper.toDomain
import com.elhady.lafyuu.feature.home.data.remote.HomeApi
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.model.ProductQuery
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi,
    private val safeApiCaller: SafeApiCaller
) : HomeRepository {

    override suspend fun getCategories(): AppResult<List<Category>> {
        return safeApiCaller.call {
            homeApi.getCategories().allCategories.map { it.toDomain() }
        }
    }

    override suspend fun getOffers(): AppResult<List<Offer>> {
        return safeApiCaller.call { homeApi.getOffers().allOffers.map { it.toDomain() } }
    }

    override suspend fun getProducts(query: ProductQuery): AppResult<List<Product>> =
        safeApiCaller.call {
            homeApi.getProducts(
                searchTerm = query.searchTerm,
                category = query.category,
                sortBy = query.sortBy.toString(),
                sortOrder = query.sortBy.toString(),
                page = query.page,
                pageSize = query.pageSize
            ).allProducts.map { it.toDomain() }
        }
}
