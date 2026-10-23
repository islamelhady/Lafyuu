package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import javax.inject.Inject

class ValidatePassword @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error.PasswordRequired
            !password.any { it.isDigit() } -> ValidationResult.Error.PasswordMissingDigit
            !password.any { it.isUpperCase() } -> ValidationResult.Error.PasswordMissingUppercase
            !password.any { !it.isLetterOrDigit() } -> ValidationResult.Error.PasswordMissingSpecialChar
            else -> ValidationResult.Success
        }
    }
}
