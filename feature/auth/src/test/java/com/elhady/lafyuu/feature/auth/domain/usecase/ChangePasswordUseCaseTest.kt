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

class ChangePasswordUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var changePasswordUseCase: ChangePasswordUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        changePasswordUseCase = ChangePasswordUseCase(authRepository)
    }

    @Test
    fun `invoke calls repository with passwords and returns success`() = runTest {
        coEvery { authRepository.changePassword("OldPass1@", "NewPass1@", "NewPass1@") } returns AppResult.Success(Unit)

        val result = changePasswordUseCase("OldPass1@", "NewPass1@", "NewPass1@")

        assertEquals(AppResult.Success(Unit), result)
        coVerify(exactly = 1) { authRepository.changePassword("OldPass1@", "NewPass1@", "NewPass1@") }
    }
}
