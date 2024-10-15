package com.elhady.lafyuu.core.network.error

import com.elhady.lafyuu.core.network.model.ProblemDetails

sealed class NetworkError {
    data class ApiError(val details: ProblemDetails) : NetworkError()
    data object Connectivity : NetworkError()
    data object Serialization : NetworkError()
    data class Unknown(val throwable: Throwable) : NetworkError()
}

fun NetworkError.toErrorMessage(): String {
    return when (this) {
        is NetworkError.ApiError -> {
            details.detail
                ?: details.title
                ?: details.errors?.values?.flatten()?.firstOrNull()
                ?: "Request failed"
        }
        is NetworkError.Connectivity -> "No internet connection"
        is NetworkError.Serialization -> "Server error (Parsing)"
        is NetworkError.Unknown -> throwable.localizedMessage ?: "An unexpected error occurred"
    }
}

class NetworkException(val error: NetworkError) : Exception()
