package com.elhady.lafyuu.core.network.error

import com.elhady.lafyuu.core.network.model.ProblemDetails

sealed class NetworkError {
    data class ApiError(val details: ProblemDetails) : NetworkError()
    data object Connectivity : NetworkError()
    data object Serialization : NetworkError()
    data object Server : NetworkError()
    data class Unknown(val throwable: Throwable) : NetworkError()
}

class NetworkException(val error: NetworkError) : Exception()
