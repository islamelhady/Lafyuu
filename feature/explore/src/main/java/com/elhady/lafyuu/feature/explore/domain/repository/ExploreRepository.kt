package com.elhady.lafyuu.feature.explore.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.explore.domain.model.Category

interface ExploreRepository {
    suspend fun getCategories(): AppResult<List<Category>>
}
