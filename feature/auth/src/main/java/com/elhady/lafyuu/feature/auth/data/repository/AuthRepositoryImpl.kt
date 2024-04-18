package com.elhady.lafyuu.feature.auth.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.datastore.LafyuuDataStore
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.auth.data.mapper.toDomain
import com.elhady.lafyuu.feature.auth.data.remote.AuthApi
import com.elhady.lafyuu.feature.auth.data.remote.model.ChangePasswordRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.ForgotPasswordRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.LoginRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.MobileLoginRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.RegisterRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.ResendOtpRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.ResetPasswordRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.ValidateOtpRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.VerifyEmailRequest
import com.elhady.lafyuu.feature.auth.domain.model.AuthToken
import com.elhady.lafyuu.feature.auth.domain.model.User
import com.elhady.lafyuu.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val dataStore: LafyuuDataStore,
    private val apiErrorParser: ApiErrorParser
) : AuthRepository {

    override suspend fun login(email: String, password: String): AppResult<AuthToken> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()!!
                dataStore.saveTokens(body.accessToken, body.refreshToken)
                AppResult.Success(body.toDomain())
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun register(email: String, password: String, firstName: String, lastName: String): AppResult<Unit> {
        return try {
            val response = authApi.register(RegisterRequest(email, password, firstName, lastName))
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

    override suspend fun verifyEmail(email: String, otp: String): AppResult<Unit> {
        return try {
            val response = authApi.verifyEmail(VerifyEmailRequest(email, otp))
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

    override suspend fun googleLogin(idToken: String): AppResult<AuthToken> {
        return try {
            val response = authApi.googleLogin(MobileLoginRequest(idToken))
            if (response.isSuccessful) {
                val body = response.body()!!
                dataStore.saveTokens(body.accessToken, body.refreshToken)
                AppResult.Success(body.toDomain())
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun logout(): AppResult<Unit> {
        return try {
            authApi.logout()
            AppResult.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            AppResult.Success(Unit)
        } finally {
            dataStore.clearSession()
        }
    }

    override suspend fun getCurrentUser(): AppResult<User> {
        return try {
            val response = authApi.getCurrentUser()
            if (response.isSuccessful) {
                AppResult.Success(response.body()!!.toDomain())
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun forgotPassword(email: String): AppResult<Unit> {
        return try {
            val response = authApi.forgotPassword(ForgotPasswordRequest(email))
            if (response.isSuccessful) AppResult.Success(Unit) else mapError(apiErrorParser.parseError(response))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun resetPassword(email: String, otp: String, password: String): AppResult<Unit> {
        return try {
            val response = authApi.resetPassword(ResetPasswordRequest(email, otp, password))
            if (response.isSuccessful) AppResult.Success(Unit) else mapError(apiErrorParser.parseError(response))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun resendOtp(email: String): AppResult<Unit> {
        return try {
            val response = authApi.resendOtp(ResendOtpRequest(email))
            if (response.isSuccessful) AppResult.Success(Unit) else mapError(apiErrorParser.parseError(response))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun validateOtp(email: String, otp: String): AppResult<Unit> {
        return try {
            val response = authApi.validateOtp(ValidateOtpRequest(email, otp))
            if (response.isSuccessful) AppResult.Success(Unit) else mapError(apiErrorParser.parseError(response))
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
            val response = authApi.changePassword(ChangePasswordRequest(currentPassword, newPassword, confirmNewPassword))
            if (response.isSuccessful) AppResult.Success(Unit) else mapError(apiErrorParser.parseError(response))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override fun getAccessToken(): Flow<String?> = dataStore.accessToken

    override fun isAuthenticated(): Flow<Boolean> = dataStore.accessToken.map { !it.isNullOrBlank() }

    private fun mapError(networkError: NetworkError): AppResult.Error {
        return when (networkError) {
            is NetworkError.ApiError -> {
                val details = networkError.details
                val errorMessage = details.detail
                    ?: details.title
                    ?: details.errors?.values?.flatten()?.firstOrNull()
                    ?: "Request failed"
                AppResult.Error(message = errorMessage)
            }
            is NetworkError.Connectivity -> AppResult.Error(message = "No internet connection")
            is NetworkError.Serialization -> AppResult.Error(message = "Server error (Parsing)")
            is NetworkError.Server -> AppResult.Error(message = "Internal server error")
            is NetworkError.Unknown -> AppResult.Error(
                message = networkError.throwable.localizedMessage ?: "An unexpected error occurred",
                throwable = networkError.throwable
            )
        }
    }
}
