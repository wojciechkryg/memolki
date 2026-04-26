package com.wojdor.memolki.util.extension

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
