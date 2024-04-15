package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidatePasswordTest {

    private lateinit var validatePassword: ValidatePassword

    @Before
    fun setUp() {
        validatePassword = ValidatePassword()
    }

    @Test
    fun `empty password returns PasswordRequired error`() {
        val result = validatePassword("")
        assertEquals(ValidationResult.Error.PasswordRequired, result)
    }

    @Test
    fun `password missing digit returns PasswordMissingDigit error`() {
        val result = validatePassword("Password@")
        assertEquals(ValidationResult.Error.PasswordMissingDigit, result)
    }

    @Test
    fun `password missing uppercase returns PasswordMissingUppercase error`() {
        val result = validatePassword("password123@")
        assertEquals(ValidationResult.Error.PasswordMissingUppercase, result)
    }

    @Test
    fun `password missing special character returns PasswordMissingSpecialChar error`() {
        val result = validatePassword("Password123")
        assertEquals(ValidationResult.Error.PasswordMissingSpecialChar, result)
    }

    @Test
    fun `valid password with digit uppercase and special character returns Success`() {
        val result = validatePassword("Password123@")
        assertEquals(ValidationResult.Success, result)
    }
}
