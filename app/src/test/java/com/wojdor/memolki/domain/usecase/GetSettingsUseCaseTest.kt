package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.settings.SettingsLocalDataSource
import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.mock.MockSettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetSettingsUseCaseTest : AppTest() {

    private val settingsRepository = MockSettingsRepository(
        MutableStateFlow(false),
        MutableStateFlow(false),
        MutableStateFlow(false)
    )
    private lateinit var sut: GetSettingsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetSettingsUseCase(
            testDispatcher,
            settingsRepository
        )
    }

    @Test
    fun `when called then returns settings`() = runTest {
        // when
        sut().test {
            // then
            val expected = Result.success(
                listOf(
                    SettingModel.Music(false),
                    SettingModel.Sound(false),
                    SettingModel.Vibration(false)
                )
            )
            assertEquals(expected, awaitItem())
        }
    }
}
