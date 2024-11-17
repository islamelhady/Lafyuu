package com.elhady.lafyuu.feature.search.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.search.domain.model.ProductsPage
import com.elhady.lafyuu.feature.search.domain.model.SearchFilters
import com.elhady.lafyuu.feature.search.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchProductsUseCaseTest {

    private val repository: SearchRepository = mockk()
    private val useCase = SearchProductsUseCase(repository)

    @Test
    fun `blank query returns an empty page without calling the repository`() = runTest {
        val result = useCase(SearchFilters(query = "   "))

        assertEquals(AppResult.Success(ProductsPage(emptyList(), 1, false)), result)
        coVerify(exactly = 0) { repository.searchProducts(any(), any(), any()) }
    }

    @Test
    fun `query is trimmed and page size is applied`() = runTest {
        val page = ProductsPage(emptyList(), 2, true)
        coEvery { repository.searchProducts(SearchFilters(query = "shoes"), 2, 20) } returns
                AppResult.Success(page)

        val result = useCase(SearchFilters(query = "  shoes "), page = 2)

        assertEquals(AppResult.Success(page), result)
    }
}