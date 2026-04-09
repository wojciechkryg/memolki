package com.wojdor.memolki.util.extension

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class AnyExtensionsTest {

    private val crashlytics: FirebaseCrashlytics = mockk(relaxed = true)

    @Before
    fun setup() {
        mockkStatic(Log::class)
        mockkStatic(FirebaseCrashlytics::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { FirebaseCrashlytics.getInstance() } returns crashlytics
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `logD logs debug message with class name`() {
        // when
        logD("test message")

        // then
        verify { Log.d("AnyExtensionsTest", "test message") }
    }

    @Test
    fun `logE logs error and reports to crashlytics`() {
        // given
        val error = RuntimeException("test error")

        // when
        logE("error message", error)

        // then
        verify { Log.e("AnyExtensionsTest", "error message", error) }
        verify { crashlytics.setCustomKey("source", "AnyExtensionsTest") }
        verify { crashlytics.log("[AnyExtensionsTest] error message") }
        verify { crashlytics.recordException(error) }
    }
}
