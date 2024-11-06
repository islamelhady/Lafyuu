package com.elhady.lafyuu.feature.home.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.home.data.mapper.toDomain
import com.elhady.lafyuu.feature.home.data.remote.HomeApi
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi,
    private val safeApiCaller: SafeApiCaller
) : HomeRepository {

    override suspend fun getCategories(): AppResult<List<Category>> {
        return safeApiCaller.call {
            homeApi.getCategories().categories?.map { it.toDomain() }.orEmpty()
        }
    }

    override suspend fun getOffers(): AppResult<List<Offer>> {
        return safeApiCaller.call { homeApi.getOffers().allOffers.map { it.toDomain() } }
    }

    override suspend fun getProducts(
        searchTerm: String?,
        category: String?,
        sortBy: String?,
        sortOrder: String?,
        page: Int,
        pageSize: Int
    ): AppResult<List<Product>> {
        return safeApiCaller.call {
            homeApi.getProducts(
                searchTerm = searchTerm,
                category = category,
                sortBy = sortBy,
                sortOrder = sortOrder,
                page = page,
                pageSize = pageSize
            ).products?.map { it.toDomain() }.orEmpty()
        }
    }
}
