package com.elhady.lafyuu.feature.profile.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
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
        if (currentPassword.isBlank() || newPassword.isBlank() || confirmNewPassword.isBlank()) {
            return AppResult.Error("Please fill all password fields")
        }
        if (newPassword != confirmNewPassword) {
            return AppResult.Error("New passwords do not match")
        }
        return repository.changePassword(currentPassword, newPassword, confirmNewPassword)
    }
}
