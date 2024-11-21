package com.elhady.lafyuu.feature.address.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.model.AddressInput
import com.elhady.lafyuu.feature.address.domain.repository.AddressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AddressUseCasesTest {

    private val repository: AddressRepository = mockk()
    private val address = Address("a-1", "Cairo", "Nasr City", "Street 9", "12", "01012345678", "")
    private val input = AddressInput("Cairo", "Nasr City", "Street 9", "12", "01012345678")

    @Test
    fun `create trims the input before sending it`() = runTest {
        coEvery { repository.createAddress(input) } returns AppResult.Success(address)

        val result = CreateAddressUseCase(repository)(input.copy(state = "  Cairo  "))

        assertEquals(AppResult.Success(address), result)
    }

    @Test
    fun `create rejects an invalid input without calling the repository`() = runTest {
        val result = CreateAddressUseCase(repository)(input.copy(phoneNumber = "12"))

        assertEquals(AppResult.Failure(AppError.Validation("Invalid address")), result)
        coVerify(exactly = 0) { repository.createAddress(any()) }
    }

    @Test
    fun `update rejects a blank id and an invalid input`() = runTest {
        val useCase = UpdateAddressUseCase(repository)

        assertEquals(
            AppResult.Failure(AppError.Validation("Invalid address ID")),
            useCase(" ", input)
        )
        assertEquals(
            AppResult.Failure(AppError.Validation("Invalid address")),
            useCase("a-1", input.copy(city = ""))
        )
        coVerify(exactly = 0) { repository.updateAddress(any(), any()) }
    }

    @Test
    fun `update delegates with the id`() = runTest {
        coEvery { repository.updateAddress("a-1", input) } returns AppResult.Success(address)

        assertEquals(AppResult.Success(address), UpdateAddressUseCase(repository)("a-1", input))
    }

    @Test
    fun `get and delete reject a blank id`() = runTest {
        val expected = AppResult.Failure(AppError.Validation("Invalid address ID"))

        assertEquals(expected, GetAddressUseCase(repository)(""))
        assertEquals(expected, DeleteAddressUseCase(repository)(" "))
        coVerify(exactly = 0) { repository.getAddress(any()) }
        coVerify(exactly = 0) { repository.deleteAddress(any()) }
    }
}