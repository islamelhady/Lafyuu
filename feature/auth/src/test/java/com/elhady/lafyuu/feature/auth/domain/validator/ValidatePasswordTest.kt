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
    fun `non-empty password returns Success`() {
        val result = validatePassword("password123")
        assertEquals(ValidationResult.Success, result)
    }
}
