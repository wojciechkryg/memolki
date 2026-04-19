package com.wojdor.memolki.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.wojdor.memolki.data.local.datastore.settings.SettingsLocalDataSource
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class SettingsLocalDataSourceTest : AppTest() {

    private val dataStore: DataStore<Preferences> by inject()

    private lateinit var sut: SettingsLocalDataSource

    @Before
    override fun setup() {
        super.setup()
        sut = SettingsLocalDataSource(dataStore)
    }

    @Test
    fun `when get music enabled without a value then return default true`() = runTest {
        // when
        val result = sut.getMusicEnabled().first()

        // then
        assertTrue(result)
    }

    @Test
    fun `when get music enabled with a value then return it`() = runTest {
        // given
        sut.setMusicEnabled(false)

        // when
        val result = sut.getMusicEnabled().first()

        // then
        assertFalse(result)
    }

    @Test
    fun `when set music enabled then the value is saved`() = runTest {
        // when
        sut.setMusicEnabled(false)
        val result = sut.getMusicEnabled().first()

        // then
        assertFalse(result)
    }

    @Test
    fun `when get sound enabled without a value then return default true`() = runTest {
        // when
        val result = sut.getSoundEnabled().first()

        // then
        assertTrue(result)
    }

    @Test
    fun `when get sound enabled with a value then return it`() = runTest {
        // given
        sut.setSoundEnabled(false)

        // when
        val result = sut.getSoundEnabled().first()

        // then
        assertFalse(result)
    }

    @Test
    fun `when set sound enabled then the value is saved`() = runTest {
        // when
        sut.setSoundEnabled(false)
        val result = sut.getSoundEnabled().first()

        // then
        assertFalse(result)
    }

    @Test
    fun `when get vibrations enabled without a value then return default true`() = runTest {
        // when
        val result = sut.getVibrationEnabled().first()

        // then
        assertTrue(result)
    }

    @Test
    fun `when get vibrations enabled with a value then return it`() = runTest {
        // given
        sut.setVibrationEnabled(false)

        // when
        val result = sut.getVibrationEnabled().first()

        // then
        assertFalse(result)
    }

    @Test
    fun `when set vibrations enabled then the value is saved`() = runTest {
        // when
        sut.setVibrationEnabled(false)
        val result = sut.getVibrationEnabled().first()

        // then
        assertFalse(result)
    }
}
