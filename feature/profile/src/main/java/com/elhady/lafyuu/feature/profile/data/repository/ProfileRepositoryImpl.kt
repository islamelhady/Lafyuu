package com.elhady.lafyuu.feature.profile.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.core.network.error.toErrorMessage
import com.elhady.lafyuu.feature.auth.data.remote.model.ChangePasswordRequest
import com.elhady.lafyuu.feature.profile.data.remote.ProfileApi
import com.elhady.lafyuu.feature.profile.domain.model.UserProfile
import com.elhady.lafyuu.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi,
    private val apiErrorParser: ApiErrorParser
) : ProfileRepository {

    override suspend fun getUserProfile(): AppResult<UserProfile> {
        return try {
            val response = profileApi.getCurrentUserInfo()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(
                        UserProfile(
                            userId = body.userId,
                            email = body.email,
                            fullName = body.fullName,
                            profilePicture = body.profilePicture
                        )
                    )
                } else {
                    AppResult.Error("User profile is empty")
                }
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): AppResult<Unit> {
        return try {
            val response = profileApi.changePassword(
                ChangePasswordRequest(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    confirmNewPassword = confirmNewPassword
                )
            )
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    private fun mapError(networkError: NetworkError): AppResult.Error {
        return when (networkError) {
            is NetworkError.ApiError -> AppResult.Error(
                message = networkError.toErrorMessage(),
                status = networkError.details.status,
                validationErrors = networkError.details.errors
            )
            is NetworkError.Connectivity -> AppResult.Error(message = networkError.toErrorMessage())
            is NetworkError.Serialization -> AppResult.Error(message = networkError.toErrorMessage())
            is NetworkError.Unknown -> AppResult.Error(message = networkError.toErrorMessage(), throwable = networkError.throwable)
        }
    }
}
