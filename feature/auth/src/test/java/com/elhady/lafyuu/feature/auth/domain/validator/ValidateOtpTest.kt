package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateOtpTest {

    private lateinit var validateOtp: ValidateOtp

    @Before
    fun setUp() {
        validateOtp = ValidateOtp()
    }

    @Test
    fun `empty otp returns OtpRequired error`() {
        val result = validateOtp("")
        assertEquals(ValidationResult.Error.OtpRequired, result)
    }

    @Test
    fun `blank otp returns OtpRequired error`() {
        val result = validateOtp("   ")
        assertEquals(ValidationResult.Error.OtpRequired, result)
    }

    @Test
    fun `valid otp returns Success`() {
        val result = validateOtp("1234")
        assertEquals(ValidationResult.Success, result)
    }
}
