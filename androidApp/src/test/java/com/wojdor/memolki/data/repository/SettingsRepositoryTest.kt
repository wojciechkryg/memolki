package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.local.datastore.settings.SettingsLocalDataSource
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class SettingsRepositoryTest : AppTest() {

    private val settingsLocalDataSource: SettingsLocalDataSource by inject()

    private lateinit var sut: SettingsRepository

    @Before
    override fun setup() {
        super.setup()
        sut = get()
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
