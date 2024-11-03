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

sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Failure -> this
}