package com.elhady.lafyuu.core.ui.text

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.ui.R

sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class Resource(@StringRes val id: Int, val args: List<Any> = emptyList()) : UiText

    fun asString(context: Context): String = when (this) {
        is Dynamic -> value
        is Resource -> context.getString(id, *args.toTypedArray())
    }
}

@Composable
fun UiText.asString(): String = asString(LocalContext.current)

fun AppError.toUiText(): UiText = when (this) {
    AppError.NoInternet -> UiText.Resource(R.string.error_no_internet)
    AppError.Timeout -> UiText.Resource(R.string.error_timeout)
    AppError.Unauthorized -> UiText.Resource(R.string.error_session_expired)
    AppError.Forbidden -> UiText.Resource(R.string.error_forbidden)
    AppError.NotFound -> UiText.Resource(R.string.error_not_found)
    is AppError.OutOfStock -> UiText.Resource(R.string.error_out_of_stock, listOf(available))
    is AppError.Validation -> {
        val serverText = fieldErrors.values.firstOrNull()?.firstOrNull() ?: message
        serverText?.takeIf { it.isNotBlank() }?.let(UiText::Dynamic)
            ?: UiText.Resource(R.string.error_invalid_request)
    }

    is AppError.Server -> UiText.Resource(R.string.error_server)
    AppError.Unknown -> UiText.Resource(R.string.error_unknown)
}