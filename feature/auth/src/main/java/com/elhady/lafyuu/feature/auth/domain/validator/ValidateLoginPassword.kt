package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import javax.inject.Inject

class ValidateLoginPassword @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {
        return when{
            password.isBlank() -> ValidationResult.Error.PasswordRequired
            else -> ValidationResult.Success
        }
    }
}