package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import javax.inject.Inject

class ValidateOtp @Inject constructor() {
    operator fun invoke(otp: String): ValidationResult {
        return when{
            otp.isBlank() -> ValidationResult.Error.OtpRequired
            else -> ValidationResult.Success
        }
    }
}
