package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import javax.inject.Inject

class ValidateOtp @Inject constructor() {
    operator fun invoke(otp: String): ValidationResult {
        if (otp.isBlank()) {
            return ValidationResult.Error.OtpRequired
        }
        return ValidationResult.Success
    }
}
