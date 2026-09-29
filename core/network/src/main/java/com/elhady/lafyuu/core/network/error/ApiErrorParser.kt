package com.elhady.lafyuu.core.network.error

import com.elhady.lafyuu.core.network.model.ProblemDetails
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.io.IOException

class ApiErrorParser(private val json: Json) {

    fun <T> parseError(response: Response<T>): NetworkError {
        val errorString = try {
            response.errorBody()?.string()
        } catch (e: Exception) {
            null
        }

        return if (!errorString.isNullOrBlank()) {
            try {
                val details = json.decodeFromString<ProblemDetails>(errorString)
                NetworkError.ApiError(details)
            } catch (e: Exception) {
                NetworkError.ApiError(ProblemDetails(title = errorString, status = response.code()))
            }
        } else {
            NetworkError.ApiError(
                ProblemDetails(
                    title = when (response.code()) {
                        401 -> "Unauthorized. Invalid or unregistered account credentials."
                        403 -> "Access forbidden."
                        404 -> "Resource not found."
                        in 500..599 -> "Internal server error (${response.code()})"
                        else -> "HTTP ${response.code()} error"
                    },
                    status = response.code()
                )
            )
        }
    }

    fun parseException(throwable: Throwable): NetworkError {
        return when (throwable) {
            is IOException -> NetworkError.Connectivity
            is kotlinx.serialization.SerializationException -> NetworkError.Serialization
            else -> NetworkError.Unknown(throwable)
        }
    }
}
