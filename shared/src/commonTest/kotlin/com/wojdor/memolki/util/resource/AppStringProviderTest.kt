package com.wojdor.memolki.util.resource

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.app_logo
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.fail

class AppStringProviderTest {

    private val sut = AppStringProvider()

    // Regression: unqualified delegation once resolved to the member itself (infinite recursion).
    // Any outcome other than StackOverflowError proves the call reached compose-resources.
    @Test
    fun `getString delegates to compose resources instead of recursing`() = runTest {
        try {
            sut.getString(Res.string.app_logo)
        } catch (error: StackOverflowError) {
            fail("AppStringProvider.getString recursed into itself")
        } catch (_: Throwable) {
        }
    }
}
