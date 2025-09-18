package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.settings.SettingsLocalDataSource
import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SetVibrationsEnabledUseCaseTest : AppTest() {

    private val settingsRepository = SettingsRepository(SettingsLocalDataSource(MockDataStore()))
    private lateinit var sut: SetVibrationsEnabledUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = SetVibrationsEnabledUseCase(
            testDispatcher,
            settingsRepository
        )
    }

    @Test
    fun `when called then set vibrations enabled`() = runTest {
        // when
        sut(false).test {
            // then
            val expected = Result.success(Unit)
            assertEquals(expected, awaitItem())
            assertEquals(false, settingsRepository.getVibrationEnabled())
            awaitComplete()
        }
    }
}
