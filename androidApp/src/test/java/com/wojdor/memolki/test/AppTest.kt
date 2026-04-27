package com.wojdor.memolki.test

import com.wojdor.memolki.di.sharedKoinModule
import com.wojdor.memolki.test.di.testKoinModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@ExperimentalCoroutinesApi
abstract class AppTest : KoinTest {

    protected val testDispatcher: CoroutineDispatcher by inject()

    @BeforeTest
    open fun setup() {
        startKoin { modules(sharedKoinModule, testKoinModule) }
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    open fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }
}
