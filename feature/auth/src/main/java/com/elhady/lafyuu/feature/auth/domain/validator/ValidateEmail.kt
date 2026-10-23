package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import java.util.regex.Pattern
import javax.inject.Inject

class ValidateEmail @Inject constructor() {

    private val emailPattern = Pattern.compile(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    operator fun invoke(email: String): ValidationResult {
        return when{
            email.isBlank() -> ValidationResult.Error.EmailRequired
            !emailPattern.matcher(email.trim()).matches() -> ValidationResult.Error.InvalidEmailFormat
            else -> ValidationResult.Success
        }
    }
}
