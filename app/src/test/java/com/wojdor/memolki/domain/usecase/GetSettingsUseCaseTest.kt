package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetSettingsUseCaseTest : AppTest() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private lateinit var sut: GetSettingsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetSettingsUseCase(
            testDispatcher,
            settingsRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when called first time then returns default settings`() = runTest {
        // when
        sut().test {
            // then
            val expected = Result.success(
                listOf(
                    SettingModel.Music(true),
                    SettingModel.Sound(true),
                    SettingModel.Vibration(true)
                )
            )
            assertEquals(expected, awaitItem())
        }
    }
}
