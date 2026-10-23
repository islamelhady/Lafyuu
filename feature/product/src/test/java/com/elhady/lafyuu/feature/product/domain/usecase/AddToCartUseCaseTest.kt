package com.elhady.lafyuu.feature.product.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddToCartUseCaseTest {

    private val repository: ProductRepository = mockk()
    private lateinit var useCase: AddToCartUseCase

    @Before
    fun setUp() {
        useCase = AddToCartUseCase(repository)
    }

    @Test
    fun `returns error when productId is blank`() = runTest {
        val result = useCase("", 1)

        assertTrue(result is AppResult.Failure)
        assertEquals("Invalid product ID", (result as AppResult.Failure).error)
    }

    @Test
    fun `returns error when quantity is zero or negative`() = runTest {
        val result = useCase("p-1", 0)

        assertTrue(result is AppResult.Failure)
        assertEquals("Quantity must be greater than zero", (result as AppResult.Failure).error)
    }

    @Test
    fun `calls repository addToCart when inputs are valid`() = runTest {
        coEvery { repository.addToCart("p-1", 2) } returns AppResult.Success(Unit)

        val result = useCase("p-1", 2)

        assertTrue(result is AppResult.Success)
        coVerify(exactly = 1) { repository.addToCart("p-1", 2) }
    }
}
