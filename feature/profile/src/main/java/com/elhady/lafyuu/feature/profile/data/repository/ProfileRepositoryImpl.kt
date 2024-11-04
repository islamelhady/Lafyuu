package com.elhady.lafyuu.feature.profile.data.repository

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.auth.data.mapper.toDomain
import com.elhady.lafyuu.feature.auth.data.remote.model.ChangePasswordRequest
import com.elhady.lafyuu.feature.auth.domain.model.User
import com.elhady.lafyuu.feature.profile.data.remote.ProfileApi
import com.elhady.lafyuu.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi,
    private val safeApiCaller: SafeApiCaller
) : ProfileRepository {

    override suspend fun getUserProfile(): AppResult<User> {
        return safeApiCaller.call { profileApi.getCurrentUserInfo().toDomain() }
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): AppResult<Unit> {
        return safeApiCaller.call {
            profileApi.changePassword(
                ChangePasswordRequest(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    confirmNewPassword = confirmNewPassword
                )
            )
        }
    }
}
