package com.elhady.lafyuu.core.network.error

import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class AppResultTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val parser = AppResult(json)

    @Test
    fun `parseError handles valid ProblemDetails response and preserves status`() {
        val errorJson = "{\"title\":\"Bad Request\",\"status\":400,\"detail\":\"Invalid parameter\"}"
        val response = Response.error<Any>(400, errorJson.toResponseBody("application/json".toMediaType()))
        val error = parser.parseError(response)

        val apiError = error as NetworkError.ApiError
        assertEquals(400, apiError.details.status)
        assertEquals("Bad Request", apiError.details.title)
        assertEquals("Invalid parameter", apiError.details.detail)
    }

    @Test
    fun `parseError handles validation errors response and preserves errors map`() {
        val errorJson = "{\"title\":\"Validation Error\",\"status\":422,\"errors\":{\"email\":[\"Invalid email format\"]}}"
        val response = Response.error<Any>(422, errorJson.toResponseBody("application/json".toMediaType()))
        val error = parser.parseError(response)

        val apiError = error as NetworkError.ApiError
        assertEquals(422, apiError.details.status)
        assertEquals(1, apiError.details.errors?.size)
        assertTrue(apiError.details.errors?.containsKey("email") == true)
    }

    @Test
    fun `parseError handles empty error body and preserves status`() {
        val response = Response.error<Any>(404, "".toResponseBody(null))
        val error = parser.parseError(response)

        val apiError = error as NetworkError.ApiError
        assertEquals(404, apiError.details.status)
        assertNull(apiError.details.title)
    }

    @Test
    fun `parseError handles malformed json error body without crashing and preserves status`() {
        val malformedJson = "{malformed-json"
        val response = Response.error<Any>(500, malformedJson.toResponseBody("application/json".toMediaType()))
        val error = parser.parseError(response)

        val apiError = error as NetworkError.ApiError
        assertEquals(500, apiError.details.status)
        assertNull(apiError.details.title)
    }

    @Test
    fun `parseError handles non-json error body without crashing and preserves status`() {
        val htmlBody = "<html><body>502 Bad Gateway</body></html>"
        val response = Response.error<Any>(502, htmlBody.toResponseBody("text/html".toMediaType()))
        val error = parser.parseError(response)

        val apiError = error as NetworkError.ApiError
        assertEquals(502, apiError.details.status)
        assertNull(apiError.details.title)
    }

    @Test(expected = CancellationException::class)
    fun `parseError rethrows CancellationException when reading error body`() {
        val responseBody = object : okhttp3.ResponseBody() {
            override fun contentType() = "application/json".toMediaType()
            override fun contentLength() = 10L
            override fun source() = throw CancellationException("Cancelled")
        }
        val response = Response.error<Any>(400, responseBody)
        parser.parseError(response)
    }

    @Test
    fun `parseException handles IOException as Connectivity`() {
        val error = parser.parseException(IOException("Network down"))
        assertEquals(NetworkError.Connectivity, error)
    }

    @Test
    fun `parseException handles SerializationException as Serialization`() {
        val ex = SerializationException("Parse error")
        val error = parser.parseException(ex)
        assertTrue(error is NetworkError.Serialization)
        assertEquals(ex, (error as NetworkError.Serialization).throwable)
    }
}
