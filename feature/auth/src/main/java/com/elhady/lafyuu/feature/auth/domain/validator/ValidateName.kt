package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import javax.inject.Inject

class ValidateName @Inject constructor() {
    operator fun invoke(name: String): ValidationResult {
        return when{
            name.isBlank() -> ValidationResult.Error.NameRequired
            else -> ValidationResult.Success
        }
    }
}
