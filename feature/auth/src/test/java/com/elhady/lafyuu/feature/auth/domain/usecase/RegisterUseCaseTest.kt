package com.elhady.lafyuu.feature.auth.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RegisterUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var registerUseCase: RegisterUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        registerUseCase = RegisterUseCase(authRepository)
    }

    @Test
    fun `invoke calls repository with trimmed inputs and returns success`() = runTest {
        coEvery { authRepository.register("user@example.com", "Password123@", "John", "Doe") } returns AppResult.Success(Unit)

        val result = registerUseCase(" user@example.com ", "Password123@", " John ", " Doe ")

        assertEquals(AppResult.Success(Unit), result)
        coVerify(exactly = 1) { authRepository.register("user@example.com", "Password123@", "John", "Doe") }
    }
}
