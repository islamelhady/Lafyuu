package com.elhady.lafyuu.feature.auth.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, otp: String, password: String): AppResult<Unit> {
        return authRepository.resetPassword(email.trim(), otp.trim(), password)
    }
}
