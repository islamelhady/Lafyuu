package com.elhady.lafyuu.feature.checkout.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckoutUseCasesTest {

    private val repository: CheckoutRepository = mockk()

    @Test
    fun `CheckoutUseCase validates address and payment method`() = runTest {
        val useCase = CheckoutUseCase(repository)

        val resultBlankAddress = useCase("", "Credit Card", null)
        assertTrue(resultBlankAddress is AppResult.Error)
        assertEquals("Please select a shipping address", (resultBlankAddress as AppResult.Error).message)

        val resultBlankPayment = useCase("addr-1", "", null)
        assertTrue(resultBlankPayment is AppResult.Error)
        assertEquals("Please select a payment method", (resultBlankPayment as AppResult.Error).message)
    }

    @Test
    fun `CheckoutUseCase succeeds with valid inputs`() = runTest {
        val useCase = CheckoutUseCase(repository)
        val expected = CheckoutResult("OK", null, null)
        coEvery { repository.checkout("addr-1", "Credit Card", "SUMMER") } returns AppResult.Success(expected)

        val result = useCase("addr-1", "Credit Card", "SUMMER")
        assertTrue(result is AppResult.Success)
        assertEquals("OK", (result as AppResult.Success).data.message)
    }
}
