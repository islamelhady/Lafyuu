package com.elhady.lafyuu.feature.explore.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.explore.domain.model.Category
import com.elhady.lafyuu.feature.explore.domain.repository.ExploreRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: ExploreRepository
) {
    suspend operator fun invoke(): AppResult<List<Category>> {
        return repository.getCategories()
    }
}
