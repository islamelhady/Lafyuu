package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateEmailTest {

    private lateinit var validateEmail: ValidateEmail

    @Before
    fun setUp() {
        validateEmail = ValidateEmail()
    }

    @Test
    fun `empty email returns EmailRequired error`() {
        val result = validateEmail("")
        assertEquals(ValidationResult.Error.EmailRequired, result)
    }

    @Test
    fun `blank email returns EmailRequired error`() {
        val result = validateEmail("   ")
        assertEquals(ValidationResult.Error.EmailRequired, result)
    }

    @Test
    fun `invalid email format returns InvalidEmailFormat error`() {
        val result = validateEmail("invalid-email")
        assertEquals(ValidationResult.Error.InvalidEmailFormat, result)
    }

    @Test
    fun `valid email returns Success`() {
        val result = validateEmail("user@example.com")
        assertEquals(ValidationResult.Success, result)
    }
}
