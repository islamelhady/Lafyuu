package com.elhady.lafyuu.feature.search.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.search.domain.model.Category
import com.elhady.lafyuu.feature.search.domain.repository.SearchRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(): AppResult<List<Category>> {
        return repository.getCategories()
    }
}
