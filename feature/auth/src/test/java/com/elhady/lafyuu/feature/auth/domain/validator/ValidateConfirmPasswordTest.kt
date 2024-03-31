package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateConfirmPasswordTest {

    private lateinit var validateConfirmPassword: ValidateConfirmPassword

    @Before
    fun setUp() {
        validateConfirmPassword = ValidateConfirmPassword()
    }

    @Test
    fun `empty confirmation password returns ConfirmPasswordRequired error`() {
        val result = validateConfirmPassword("pass", "")
        assertEquals(ValidationResult.Error.ConfirmPasswordRequired, result)
    }

    @Test
    fun `mismatched passwords returns PasswordMismatch error`() {
        val result = validateConfirmPassword("pass1", "pass2")
        assertEquals(ValidationResult.Error.PasswordMismatch, result)
    }

    @Test
    fun `matching passwords returns Success`() {
        val result = validateConfirmPassword("password123", "password123")
        assertEquals(ValidationResult.Success, result)
    }
}
