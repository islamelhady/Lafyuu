package com.elhady.lafyuu.feature.checkout.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.Address
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

        val resultBlankAddress = useCase("", "Credit Card Or Debit", null)
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
        coEvery { repository.checkout("addr-1", "Credit Card Or Debit", "SUMMER") } returns AppResult.Success(expected)

        val result = useCase("addr-1", "Credit Card Or Debit", "SUMMER")
        assertTrue(result is AppResult.Success)
        assertEquals("OK", (result as AppResult.Success).data.message)
    }

    @Test
    fun `CreateAddressUseCase fails when max 5 addresses reached`() = runTest {
        val useCase = CreateAddressUseCase(repository)
        val fiveAddresses = (1..5).map {
            Address(id = "id-$it", state = "State", city = "City", street = "Street", apartment = "Apt", phoneNumber = "123", notes = "")
        }
        coEvery { repository.getAddresses() } returns AppResult.Success(fiveAddresses)

        val result = useCase("Cairo", "Cairo", "Street 1", "", "01000000000", "")
        assertTrue(result is AppResult.Error)
        assertEquals("Maximum limit of 5 addresses reached", (result as AppResult.Error).message)
    }

    @Test
    fun `CreateAddressUseCase succeeds with optional apartment`() = runTest {
        val useCase = CreateAddressUseCase(repository)
        coEvery { repository.getAddresses() } returns AppResult.Success(emptyList())
        val newAddress = Address(id = "new-1", state = "Cairo", city = "Cairo", street = "Street 1", apartment = "", phoneNumber = "01000000000", notes = "")
        coEvery { repository.createAddress("Cairo", "Cairo", "Street 1", "", "01000000000", "") } returns AppResult.Success(newAddress)

        val result = useCase("Cairo", "Cairo", "Street 1", "", "01000000000", "")
        assertTrue(result is AppResult.Success)
    }
}
