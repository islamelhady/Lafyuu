package com.elhady.lafyuu.feature.checkout.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.model.PaymentMethod
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutUseCaseTest {

    private val repository: CheckoutRepository = mockk()
    private val useCase = CheckoutUseCase(repository)
    private val completed = CheckoutResult.Completed("Done")

    @Test
    fun `blank address is rejected without calling the repository`() = runTest {
        val result = useCase(" ", PaymentMethod.CREDIT_CARD, null)

        assertEquals(AppResult.Failure(AppError.Validation("Shipping address is missing")), result)
        coVerify(exactly = 0) { repository.checkout(any(), any(), any()) }
    }

    @Test
    fun `blank coupon is sent as null and a real one is trimmed`() = runTest {
        coEvery { repository.checkout("a-1", PaymentMethod.CREDIT_CARD, null) } returns AppResult.Success(completed)
        coEvery { repository.checkout("a-1", PaymentMethod.CREDIT_CARD, "SAVE10") } returns AppResult.Success(completed)

        useCase("a-1", PaymentMethod.CREDIT_CARD, "   ")
        useCase("a-1", PaymentMethod.CREDIT_CARD, " SAVE10 ")

        coVerify(exactly = 1) { repository.checkout("a-1", PaymentMethod.CREDIT_CARD, null) }
        coVerify(exactly = 1) { repository.checkout("a-1", PaymentMethod.CREDIT_CARD, "SAVE10") }
    }
}