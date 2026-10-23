package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import javax.inject.Inject

class ValidateConfirmPassword @Inject constructor(
    private val validatePassword: ValidatePassword
) {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        val passwordResult = validatePassword(password)
        return when {
            passwordResult !is ValidationResult.Success -> passwordResult
            confirmPassword.isBlank() -> ValidationResult.Error.ConfirmPasswordRequired
            password != confirmPassword -> ValidationResult.Error.PasswordMismatch
            else -> ValidationResult.Success
        }
    }
}
