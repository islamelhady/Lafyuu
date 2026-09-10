package com.elhady.lafyuu.core.network.error

import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.core.network.model.ProblemDetails
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.io.IOException

class ApiErrorParser(private val json: Json) {

    fun <T> parseError(response: Response<T>): NetworkError {
        val errorBody = response.errorBody()
        return if (errorBody != null) {
            try {
                val details = json.decodeFromString<ProblemDetails>(errorBody.string())
                NetworkError.ApiError(details)
            } catch (e: Exception) {
                NetworkError.Server
            }
        } else {
            NetworkError.Server
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
