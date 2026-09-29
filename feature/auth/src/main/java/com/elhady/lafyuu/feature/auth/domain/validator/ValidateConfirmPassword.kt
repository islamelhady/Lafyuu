package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import javax.inject.Inject

class ValidateConfirmPassword @Inject constructor() {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (confirmPassword.isBlank()) {
            return ValidationResult.Error.ConfirmPasswordRequired
        }
        if (password != confirmPassword) {
            return ValidationResult.Error.PasswordMismatch
        }
        return ValidationResult.Success
    }
}
