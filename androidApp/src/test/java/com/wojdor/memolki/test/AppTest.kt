package com.wojdor.memolki.test

import com.wojdor.memolki.test.di.testKoinModule
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@ExperimentalCoroutinesApi
abstract class AppTest : KoinTest {

    protected val testDispatcher: CoroutineDispatcher by inject()

    @Before
    open fun setup() {
        startKoin { modules(testKoinModule) }
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @After
    open fun tearDown() {
        stopKoin()
        unmockkAll()
        Dispatchers.resetMain()
    }
}
