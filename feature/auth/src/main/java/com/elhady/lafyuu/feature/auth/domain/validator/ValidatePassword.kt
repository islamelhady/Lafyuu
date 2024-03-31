package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import javax.inject.Inject

class ValidatePassword @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult.Error.PasswordRequired
        }
        return ValidationResult.Success
    }
}
