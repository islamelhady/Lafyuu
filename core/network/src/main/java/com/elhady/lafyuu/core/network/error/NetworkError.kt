package com.elhady.lafyuu.core.network.error

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.model.ProblemDetails

sealed class NetworkError {
    data class ApiError(val details: ProblemDetails) : NetworkError()
    data object Connectivity : NetworkError()
    data class Serialization(val throwable: Throwable? = null) : NetworkError()
    data class Unknown(val throwable: Throwable) : NetworkError()
}

fun NetworkError.toAppResultError(): AppResult.Error {
    return when (this) {
        is NetworkError.ApiError -> AppResult.Error(
            message = details.detail ?: details.title ?: details.errors?.values?.flatten()?.firstOrNull(),
            status = details.status,
            validationErrors = details.errors
        )
        is NetworkError.Connectivity -> AppResult.Error(
            message = null,
            throwable = null
        )
        is NetworkError.Serialization -> AppResult.Error(
            message = throwable?.localizedMessage,
            throwable = throwable
        )
        is NetworkError.Unknown -> AppResult.Error(
            message = throwable.localizedMessage,
            throwable = throwable
        )
    }
}

class NetworkException(val error: NetworkError) : Exception()
