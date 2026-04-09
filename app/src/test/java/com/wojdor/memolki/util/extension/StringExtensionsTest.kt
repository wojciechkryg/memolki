package com.wojdor.memolki.util.extension

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun `when string contains whitespace then returns true`() {
        // then
        assertTrue("hello world".containsWhitespace)
    }

    @Test
    fun `when string has no whitespace then returns false`() {
        // then
        assertFalse("hello".containsWhitespace)
    }

    @Test
    fun `when empty string then returns false`() {
        // then
        assertFalse("".containsWhitespace)
    }
}
