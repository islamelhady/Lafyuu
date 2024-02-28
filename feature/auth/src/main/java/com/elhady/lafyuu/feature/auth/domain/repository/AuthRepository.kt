package com.elhady.lafyuu.feature.auth.domain.repository

import com.elhady.lafyuu.feature.auth.domain.model.AuthToken
import com.elhady.lafyuu.feature.auth.domain.model.User
import com.elhady.lafyuu.core.common.AppResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): AppResult<AuthToken>
    suspend fun register(email: String, password: String, firstName: String, lastName: String): AppResult<Unit>
    suspend fun verifyEmail(email: String, otp: String): AppResult<Unit>
    suspend fun googleLogin(idToken: String): AppResult<AuthToken>
    suspend fun logout(): AppResult<Unit>
    suspend fun getCurrentUser(): AppResult<User>
    suspend fun forgotPassword(email: String): AppResult<Unit>
    suspend fun resetPassword(email: String, otp: String, password: String): AppResult<Unit>
    suspend fun resendOtp(email: String): AppResult<Unit>
    suspend fun validateOtp(email: String, otp: String): AppResult<Unit>
    
    fun getAccessToken(): Flow<String?>
    fun isAuthenticated(): Flow<Boolean>
}
