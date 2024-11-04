package com.elhady.lafyuu.feature.profile.domain.usecase

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): AppResult<Unit> {
        return repository.changePassword(currentPassword, newPassword, confirmNewPassword)
    }
}
