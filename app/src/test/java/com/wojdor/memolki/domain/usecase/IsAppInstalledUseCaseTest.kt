package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.util.provider.AppInstalledProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IsAppInstalledUseCaseTest : AppTest() {

    private val appInstalledProvider: AppInstalledProvider = mockk()
    private lateinit var sut: IsAppInstalledUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = IsAppInstalledUseCase(testDispatcher, appInstalledProvider)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when the fruitHalf app is installed then return true`() = runTest {
        // given
        val app = AppModel.FruitHalf
        every { appInstalledProvider.isAppInstalled(app.appId) } returns true

        // when
        val result = sut(app.appId).first()

        // then
        assertEquals(Result.success(true), result)
    }

    @Test
    fun `when the fruitHalf app is not installed then return false`() = runTest {
        // given
        val app = AppModel.FruitHalf
        every { appInstalledProvider.isAppInstalled(app.appId) } returns false

        // when
        val result = sut(app.appId).first()

        // then
        assertEquals(Result.success(false), result)
    }
}
