package com.elhady.lafyuu.feature.auth.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.datastore.LafyuuDataStore
import com.elhady.lafyuu.core.network.error.SafeApiCaller
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val dataStore: LafyuuDataStore,
    private val safeApiCaller: SafeApiCaller,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AppResult<AuthToken> {
        return safeApiCaller.call {
            val token = authApi.login(LoginRequest(email, password)).toDomain()

            dataStore.saveTokens(
                accessToken = token.accessToken,
                refreshToken = token.refreshToken
            )
            token
        }
    }

    override suspend fun register(email: String, password: String, firstName: String, lastName: String): AppResult<Unit> {
        return safeApiCaller.call { authApi.register(RegisterRequest(email, password, firstName, lastName)) }
    }

    override suspend fun verifyEmail(email: String, otp: String): AppResult<Unit> {
        return safeApiCaller.call { authApi.verifyEmail(VerifyEmailRequest(email, otp)) }
    }

    override suspend fun googleLogin(idToken: String): AppResult<AuthToken> {
        return safeApiCaller.call {
           val token = authApi.googleLogin(MobileLoginRequest(idToken)).toDomain()

            dataStore.saveTokens(
                accessToken = token.accessToken,
                refreshToken = token.refreshToken
            )
            token
        }
    }


    override suspend fun getCurrentUser(): AppResult<User> {
        return safeApiCaller.call { authApi.getCurrentUser().toDomain() }
    }

    override suspend fun forgotPassword(email: String): AppResult<Unit> {
        return safeApiCaller.call { authApi.forgotPassword(ForgotPasswordRequest(email)) }
    }

    override suspend fun resetPassword(email: String, otp: String, password: String): AppResult<Unit> {
        return safeApiCaller.call { authApi.resetPassword(ResetPasswordRequest(email, otp, password)) }
    }

    override suspend fun resendOtp(email: String): AppResult<Unit> {
        return safeApiCaller.call { authApi.resendOtp(ResendOtpRequest(email)) }
    }

    override suspend fun validateOtp(email: String, otp: String): AppResult<Unit> {
        return safeApiCaller.call { authApi.validateOtp(ValidateOtpRequest(email, otp)) }
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): AppResult<Unit> {
        return safeApiCaller.call { authApi.changePassword(ChangePasswordRequest(currentPassword, newPassword, confirmNewPassword)) }
    }

    override fun getAccessToken(): Flow<String?> = dataStore.accessToken

    override fun isAuthenticated(): Flow<Boolean> = dataStore.accessToken.map { !it.isNullOrBlank() }
}
