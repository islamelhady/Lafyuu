package com.elhady.lafyuu.feature.profile.domain.repository

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.feature.profile.domain.models.User

interface ProfileRepository {
    suspend fun getUserProfile(): AppResult<User>
    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): AppResult<Unit>

    suspend fun logout(): AppResult<Unit>
}
