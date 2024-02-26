package com.elhady.lafyuu.core.common

sealed class AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Error(val message: String? = null, val throwable: Throwable? = null) : AppResult<Nothing>()
    data object Loading : AppResult<Nothing>()
}
