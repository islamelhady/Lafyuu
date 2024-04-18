package com.elhady.lafyuu.feature.auth.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.datastore.LafyuuDataStore
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.core.network.model.ProblemDetails
import com.elhady.lafyuu.feature.auth.data.remote.AuthApi
import com.elhady.lafyuu.feature.auth.data.remote.model.LoginResponse
import com.elhady.lafyuu.feature.auth.data.remote.model.MobileLoginRequest
import com.elhady.lafyuu.feature.auth.domain.model.AuthToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryImplTest {

    private lateinit var authApi: AuthApi
    private lateinit var dataStore: LafyuuDataStore
    private lateinit var apiErrorParser: ApiErrorParser
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        authApi = mockk()
        dataStore = mockk(relaxed = true)
        apiErrorParser = mockk()
        repository = AuthRepositoryImpl(authApi, dataStore, apiErrorParser)
    }

    @Test
    fun `googleLogin sends correct idToken and saves tokens on success`() = runTest {
        val idToken = "test_google_id_token"
        val loginResponse = LoginResponse("access_123", "2026-12-31", "refresh_123")
        coEvery { authApi.googleLogin(MobileLoginRequest(idToken)) } returns Response.success(loginResponse)

        val result = repository.googleLogin(idToken)

        assertTrue(result is AppResult.Success)
        assertEquals(AuthToken("access_123", "refresh_123", "2026-12-31"), (result as AppResult.Success).data)
        coVerify(exactly = 1) { authApi.googleLogin(MobileLoginRequest(idToken)) }
        coVerify(exactly = 1) { dataStore.saveTokens("access_123", "refresh_123") }
    }

    @Test
    fun `googleLogin returns error on API failure`() = runTest {
        val idToken = "invalid_id_token"
        val failedResponse = mockk<Response<LoginResponse>>()
        every { failedResponse.isSuccessful } returns false
        every { apiErrorParser.parseError(failedResponse) } returns NetworkError.ApiError(
            ProblemDetails(detail = "Invalid Token")
        )
        coEvery { authApi.googleLogin(MobileLoginRequest(idToken)) } returns failedResponse

        val result = repository.googleLogin(idToken)

        assertTrue(result is AppResult.Error)
        assertEquals("Invalid Token", (result as AppResult.Error).message)
    }

    @Test(expected = CancellationException::class)
    fun `googleLogin rethrows CancellationException`() = runTest {
        coEvery { authApi.googleLogin(any()) } throws CancellationException("Job cancelled")

        repository.googleLogin("id_token")
    }
}
