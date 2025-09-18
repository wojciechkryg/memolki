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
class GetVibrationsEnabledUseCaseTest : AppTest() {

    private val settingsRepository = SettingsRepository(SettingsLocalDataSource(MockDataStore()))
    private lateinit var sut: GetVibrationsEnabledUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetVibrationsEnabledUseCase(
            testDispatcher,
            settingsRepository
        )
    }

    @Test
    fun `when called then returns vibrations enabled`() = runTest {
        // given
        settingsRepository.setVibrationEnabled(false)

        // when
        sut().test {
            // then
            val expected = Result.success(false)
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
