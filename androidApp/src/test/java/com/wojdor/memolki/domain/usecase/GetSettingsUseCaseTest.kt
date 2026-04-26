package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetSettingsUseCaseTest : AppTest() {

    private val settingsRepository: SettingsRepository by inject()

    private lateinit var sut: GetSettingsUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
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
