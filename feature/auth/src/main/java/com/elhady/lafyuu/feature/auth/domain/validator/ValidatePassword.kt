package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import javax.inject.Inject

class ValidatePassword @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult.Error.PasswordRequired
        }
        if (!password.any { it.isDigit() }) {
            return ValidationResult.Error.PasswordMissingDigit
        }
        if (!password.any { it.isUpperCase() }) {
            return ValidationResult.Error.PasswordMissingUppercase
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            return ValidationResult.Error.PasswordMissingSpecialChar
        }
        return ValidationResult.Success
    }
}
