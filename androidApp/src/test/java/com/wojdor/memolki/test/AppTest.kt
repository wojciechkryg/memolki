package com.wojdor.memolki.test

import com.wojdor.memolki.test.di.DaggerTestComponent
import com.wojdor.memolki.test.di.TestInjector
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import javax.inject.Inject

@ExperimentalCoroutinesApi
abstract class AppTest {

    @Inject
    lateinit var testDispatcher: CoroutineDispatcher

    private val injector: TestInjector = DaggerTestComponent.create()

    abstract fun inject(injector: TestInjector)

    @Before
    open fun setup() {
        injector.inject(this)
        inject(injector)
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @After
    open fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}
