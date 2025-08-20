package com.wojdor.memolki.test

import androidx.lifecycle.SavedStateHandle
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@ExperimentalCoroutinesApi
abstract class AppTest {

    protected val testDispatcher = StandardTestDispatcher()
    protected val savedStateHandle = SavedStateHandle()

    @Before
    open fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @After
    open fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}
