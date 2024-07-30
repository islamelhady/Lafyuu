package com.elhady.lafyuu.feature.profile.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(): AppResult<UserProfile>
    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): AppResult<Unit>
}
