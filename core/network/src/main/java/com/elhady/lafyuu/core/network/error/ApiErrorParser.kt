package com.elhady.lafyuu.core.network.error

import com.elhady.lafyuu.core.network.model.ProblemDetails
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.io.IOException

class ApiErrorParser(private val json: Json) {

    fun <T> parseError(response: Response<T>): NetworkError {
        val errorString = try {
            response.errorBody()?.string()
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            null
        }

        return if (!errorString.isNullOrBlank()) {
            try {
                val details = json.decodeFromString<ProblemDetails>(errorString)
                NetworkError.ApiError(details)
            } catch (e: CancellationException) {
                throw e
            } catch (e: SerializationException) {
                NetworkError.ApiError(ProblemDetails(status = response.code()))
            } catch (e: IllegalArgumentException) {
                NetworkError.ApiError(ProblemDetails(status = response.code()))
            }
        } else {
            NetworkError.ApiError(
                ProblemDetails(status = response.code())
            )
        }
    }

    fun parseException(throwable: Throwable): NetworkError {
        if (throwable is CancellationException) throw throwable
        return when (throwable) {
            is IOException -> NetworkError.Connectivity
            is SerializationException -> NetworkError.Serialization(throwable)
            else -> NetworkError.Unknown(throwable)
        }
    }
}
