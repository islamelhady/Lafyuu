package com.elhady.lafyuu.core.common.result

sealed interface AppError {
    data object NoInternet : AppError
    data object Timeout : AppError
    data object Unauthorized : AppError
    data object Forbidden : AppError
    data object NotFound : AppError
    data class Validation(
        val message: String?,
        val fieldErrors: Map<String, List<String>> = emptyMap()
    ) : AppError

    data class OutOfStock(val available: Int) : AppError
    data class Server(val code: Int) : AppError
    data object Unknown : AppError
}