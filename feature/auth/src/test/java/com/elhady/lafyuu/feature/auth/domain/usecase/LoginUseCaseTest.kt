package com.elhady.lafyuu.feature.auth.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.model.AuthToken
import com.elhady.lafyuu.feature.auth.domain.repository.AuthRepository
import com.elhady.lafyuu.feature.auth.domain.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        loginUseCase = LoginUseCase(authRepository)
    }

    @Test
    fun `invoke calls repository with trimmed email`() = runTest {
        val expectedToken = AuthToken("access", "refresh", "2026-12-31")
        coEvery { authRepository.login("user@example.com", "pass") } returns AppResult.Success(expectedToken)

        val result = loginUseCase(" user@example.com ", "pass")

        assertEquals(AppResult.Success(expectedToken), result)
        coVerify(exactly = 1) { authRepository.login("user@example.com", "pass") }
    }
}
