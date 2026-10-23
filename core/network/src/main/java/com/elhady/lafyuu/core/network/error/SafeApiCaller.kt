package com.elhady.lafyuu.core.network.error

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.network.model.ProblemDetails
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SafeApiCaller @Inject constructor(private val json: Json) {

    suspend fun <T> call(block: suspend () -> T): AppResult<T> = try {
        AppResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        AppResult.Failure(e.toAppError())
    } catch (e: SocketTimeoutException) {
        AppResult.Failure(AppError.Timeout)
    } catch (e: IOException) {
        AppResult.Failure(AppError.NoInternet)
    } catch (e: SerializationException) {
        AppResult.Failure(AppError.Unknown)
    }

    private fun HttpException.toAppError(): AppError = when (val code = code()) {
        400 -> {
            val details = readDetails()
            AppError.Validation(
                message = details?.detail ?: details?.title,
                fieldErrors = details?.errors.orEmpty()
            )
        }
        401 -> AppError.Unauthorized
        403 -> AppError.Forbidden
        404 -> AppError.NotFound
        in 500..599 -> AppError.Server(code)
        else -> AppError.Unknown
    }

    private fun HttpException.readDetails(): ProblemDetails? = try {
        response()?.errorBody()?.string()
            ?.takeIf { it.isNotBlank() }
            ?.let { json.decodeFromString<ProblemDetails>(it) }
    } catch (e: IOException) {
        null
    } catch (e: IllegalArgumentException) {
        null
    }
}