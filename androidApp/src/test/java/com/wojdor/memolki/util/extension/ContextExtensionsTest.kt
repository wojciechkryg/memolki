package com.wojdor.memolki.util.extension

import android.content.Context
import android.widget.Toast
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ContextExtensionsTest {

    private val context: Context = mockk(relaxed = true)
    private val toast: Toast = mockk(relaxed = true)

    @Before
    fun setup() {
        mockkStatic(Toast::class)
        every { Toast.makeText(any(), any<CharSequence>(), any()) } returns toast
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `showToast creates and shows toast`() {
        // when
        context.showToast("hello")

        // then
        verify { Toast.makeText(context, "hello", Toast.LENGTH_SHORT) }
        verify { toast.show() }
    }
}
