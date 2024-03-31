package com.elhady.lafyuu.feature.auth.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ResendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): AppResult<Unit> {
        return authRepository.resendOtp(email.trim())
    }
}
