package com.elhady.lafyuu.core.network.error

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class ApiErrorParserTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val parser = ApiErrorParser(json)

    @Test
    fun `parseError handles 404 with empty error body`() {
        val response = Response.error<Any>(404, "".toResponseBody(null))
        val error = parser.parseError(response)

        val apiError = error as NetworkError.ApiError
        assertEquals(404, apiError.details.status)
        assertEquals("Resource not found.", apiError.details.title)
    }

    @Test
    fun `parseError handles 500 with empty error body`() {
        val response = Response.error<Any>(500, "".toResponseBody(null))
        val error = parser.parseError(response)

        val apiError = error as NetworkError.ApiError
        assertEquals(500, apiError.details.status)
        assertEquals("Internal server error (500)", apiError.details.title)
    }

    @Test
    fun `parseError handles json error body`() {
        val errorJson = "{\"title\":\"Bad Request\",\"status\":400}"
        val response = Response.error<Any>(400, errorJson.toResponseBody("application/json".toMediaType()))
        val error = parser.parseError(response)

        val apiError = error as NetworkError.ApiError
        assertEquals(400, apiError.details.status)
        assertEquals("Bad Request", apiError.details.title)
    }

    @Test
    fun `parseException handles IOException as Connectivity`() {
        val error = parser.parseException(IOException("Network down"))
        assertEquals(NetworkError.Connectivity, error)
    }

    @Test
    fun `parseException handles SerializationException as Serialization`() {
        val error = parser.parseException(SerializationException("Parse error"))
        assertEquals(NetworkError.Serialization, error)
    }
}
