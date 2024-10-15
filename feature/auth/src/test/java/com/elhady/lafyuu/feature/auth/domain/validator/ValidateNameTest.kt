package com.elhady.lafyuu.feature.auth.domain.validator

import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateNameTest {

    private lateinit var validateName: ValidateName

    @Before
    fun setUp() {
        validateName = ValidateName()
    }

    @Test
    fun `empty name returns NameRequired error`() {
        val result = validateName("")
        assertEquals(ValidationResult.Error.NameRequired, result)
    }

    @Test
    fun `blank name returns NameRequired error`() {
        val result = validateName("   ")
        assertEquals(ValidationResult.Error.NameRequired, result)
    }

    @Test
    fun `valid name returns Success`() {
        val result = validateName("John Doe")
        assertEquals(ValidationResult.Success, result)
    }
}
