package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.local.settings.SettingsLocalDataSource
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingsRepositoryTest : AppTest() {

    @Inject
    lateinit var settingsLocalDataSource: SettingsLocalDataSource

    private lateinit var sut: SettingsRepository

    @Before
    override fun setup() {
        super.setup()
        sut = SettingsRepository(settingsLocalDataSource)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when get music enabled then return value from data source`() = runTest {
        // given
        settingsLocalDataSource.setMusicEnabled(false)

        // when
        val result = sut.getMusicEnabled().first()

        // then
        assertFalse(result)
    }

    @Test
    fun `when get music enabled then return default value`() = runTest {
        // when
        val result = sut.getMusicEnabled().first()

        // then
        assertTrue(result)
    }

    @Test
    fun `when set music enabled then call data source`() = runTest {
        // when
        sut.setMusicEnabled(false)

        // then
        assertFalse(settingsLocalDataSource.getMusicEnabled().first())
    }

    @Test
    fun `when get sound enabled then return value from data source`() = runTest {
        // given
        settingsLocalDataSource.setSoundEnabled(false)

        // when
        val result = sut.getSoundEnabled().first()

        // then
        assertFalse(result)
    }

    @Test
    fun `when get sound enabled then return default value`() = runTest {
        // when
        val result = sut.getSoundEnabled().first()

        // then
        assertTrue(result)
    }

    @Test
    fun `when set sound enabled then call data source`() = runTest {
        // when
        sut.setSoundEnabled(false)

        // then
        assertFalse(settingsLocalDataSource.getSoundEnabled().first())
    }

    @Test
    fun `when get vibrations enabled then return value from data source`() = runTest {
        // given
        settingsLocalDataSource.setVibrationEnabled(false)

        // when
        val result = sut.getVibrationEnabled().first()

        // then
        assertFalse(result)
    }

    @Test
    fun `when get vibrations enabled then return default value`() = runTest {
        // when
        val result = sut.getVibrationEnabled().first()

        // then
        assertTrue(result)
    }

    @Test
    fun `when set vibrations enabled then call data source`() = runTest {
        // when
        sut.setVibrationEnabled(false)

        // then
        assertFalse(settingsLocalDataSource.getVibrationEnabled().first())
    }
}
