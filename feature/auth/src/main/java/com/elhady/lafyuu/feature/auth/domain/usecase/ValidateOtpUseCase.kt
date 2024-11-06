package com.elhady.lafyuu.feature.auth.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ValidateOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, otp: String): AppResult<Unit> {
        return authRepository.validateOtp(email.trim(), otp.trim())
    }
}
