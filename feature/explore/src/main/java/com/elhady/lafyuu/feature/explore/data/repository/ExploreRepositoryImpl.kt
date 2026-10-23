package com.elhady.lafyuu.feature.explore.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.explore.data.mapper.toDomain
import com.elhady.lafyuu.feature.explore.data.remote.ExploreApi
import com.elhady.lafyuu.feature.explore.domain.model.Category
import com.elhady.lafyuu.feature.explore.domain.repository.ExploreRepository
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
    private val exploreApi: ExploreApi,
    private val safeApiCaller: SafeApiCaller
) : ExploreRepository {

    override suspend fun getCategories(): AppResult<List<Category>> {
        return safeApiCaller.call { exploreApi.getCategories().categories.map { it.toDomain() } }
    }
}
