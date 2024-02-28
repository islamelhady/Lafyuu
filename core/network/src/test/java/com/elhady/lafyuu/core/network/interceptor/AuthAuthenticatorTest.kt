package com.elhady.lafyuu.core.network.interceptor

import com.elhady.lafyuu.core.network.auth.RefreshTokenResult
import com.elhady.lafyuu.core.network.auth.TokenProvider
import com.elhady.lafyuu.core.network.auth.TokenRemoteDataSource
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthAuthenticatorTest {

    private lateinit var tokenProvider: TokenProvider
    private lateinit var tokenRemoteDataSource: TokenRemoteDataSource
    private lateinit var authenticator: AuthAuthenticator

    @Before
    fun setUp() {
        tokenProvider = mockk(relaxed = true)
        tokenRemoteDataSource = mockk()
        authenticator = AuthAuthenticator(tokenProvider, tokenRemoteDataSource)
    }

    @Test
    fun `authenticate should return request with new token when refresh succeeds`() = runBlocking {
        // Given
        val oldToken = "old_token"
        val refreshToken = "refresh_token"
        val newToken = "new_token"
        val newRefreshToken = "new_refresh_token"

        val request = Request.Builder()
            .url("https://api.elhady.com")
            .header("Authorization", "Bearer $oldToken")
            .build()
        val response = mockk<Response>()
        every { response.request } returns request
        every { response.priorResponse } returns null

        every { tokenProvider.getAccessToken() } returns flowOf(oldToken)
        every { tokenProvider.getRefreshToken() } returns flowOf(refreshToken)
        coEvery { tokenRemoteDataSource.refreshToken(refreshToken) } returns RefreshTokenResult.Success(newToken, newRefreshToken)

        // When
        val resultRequest = authenticator.authenticate(null, response)

        // Then
        assertEquals("Bearer $newToken", resultRequest?.header("Authorization"))
        coVerify { tokenProvider.updateTokens(newToken, newRefreshToken) }
    }

    @Test
    fun `authenticate should return null and clear session when refresh fails`() = runBlocking {
        // Given
        val oldToken = "old_token"
        val refreshToken = "refresh_token"

        val request = Request.Builder()
            .url("https://api.example.com")
            .header("Authorization", "Bearer $oldToken")
            .build()
        val response = mockk<Response>()
        every { response.request } returns request
        every { response.priorResponse } returns null

        every { tokenProvider.getAccessToken() } returns flowOf(oldToken)
        every { tokenProvider.getRefreshToken() } returns flowOf(refreshToken)
        coEvery { tokenRemoteDataSource.refreshToken(refreshToken) } returns RefreshTokenResult.Failure

        // When
        val resultRequest = authenticator.authenticate(null, response)

        // Then
        assertNull(resultRequest)
        coVerify { tokenProvider.clearSession() }
    }

    @Test
    fun `authenticate should use latest token without refresh if already updated by concurrent request`() = runBlocking {
        // Given
        val oldToken = "old_token"
        val latestToken = "latest_token_from_other_thread"

        val request = Request.Builder()
            .url("https://api.example.com")
            .header("Authorization", "Bearer $oldToken")
            .build()
        val response = mockk<Response>()
        every { response.request } returns request
        every { response.priorResponse } returns null

        // Latest token in provider is already different from the one in the request
        every { tokenProvider.getAccessToken() } returns flowOf(latestToken)

        // When
        val resultRequest = authenticator.authenticate(null, response)

        // Then
        assertEquals("Bearer $latestToken", resultRequest?.header("Authorization"))
        coVerify(exactly = 0) { tokenRemoteDataSource.refreshToken(any()) }
    }

    @Test
    fun `authenticate should return null and clear session if refresh token is missing`() = runBlocking {
        // Given
        val oldToken = "old_token"

        val request = Request.Builder()
            .url("https://api.example.com")
            .header("Authorization", "Bearer $oldToken")
            .build()
        val response = mockk<Response>()
        every { response.request } returns request
        every { response.priorResponse } returns null

        every { tokenProvider.getAccessToken() } returns flowOf(oldToken)
        every { tokenProvider.getRefreshToken() } returns flowOf(null)

        // When
        val resultRequest = authenticator.authenticate(null, response)

        // Then
        assertNull(resultRequest)
        coVerify { tokenProvider.clearSession() }
    }

    @Test
    fun `authenticate should return null if request has already been retried`() = runBlocking {
        // Given
        val request = Request.Builder()
            .url("https://api.example.com")
            .build()
        val response = mockk<Response>()
        val priorResponse = mockk<Response>()

        every { response.request } returns request
        every { response.priorResponse } returns priorResponse

        // When
        val resultRequest = authenticator.authenticate(null, response)

        // Then
        assertNull(resultRequest)
        coVerify(exactly = 0) { tokenRemoteDataSource.refreshToken(any()) }
    }
}
