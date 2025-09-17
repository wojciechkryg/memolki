package com.wojdor.memolki.data.local.settings

import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsLocalDataSourceTest : AppTest() {

    private lateinit var sut: SettingsLocalDataSource

    @Before
    override fun setup() {
        super.setup()
        sut = SettingsLocalDataSource(MockDataStore())
    }

    @Test
    fun `when get music enabled without a value then return default true`() = runTest {
        // when
        val result = sut.getMusicEnabled()

        // then
        assertTrue(result)
    }

    @Test
    fun `when get music enabled with a value then return it`() = runTest {
        // given
        sut.setMusicEnabled(false)

        // when
        val result = sut.getMusicEnabled()

        // then
        assertFalse(result)
    }

    @Test
    fun `when set music enabled then the value is saved`() = runTest {
        // when
        sut.setMusicEnabled(false)
        val result = sut.getMusicEnabled()

        // then
        assertFalse(result)
    }

    @Test
    fun `when get sound enabled without a value then return default true`() = runTest {
        // when
        val result = sut.getSoundEnabled()

        // then
        assertTrue(result)
    }

    @Test
    fun `when get sound enabled with a value then return it`() = runTest {
        // given
        sut.setSoundEnabled(false)

        // when
        val result = sut.getSoundEnabled()

        // then
        assertFalse(result)
    }

    @Test
    fun `when set sound enabled then the value is saved`() = runTest {
        // when
        sut.setSoundEnabled(false)
        val result = sut.getSoundEnabled()

        // then
        assertFalse(result)
    }

    @Test
    fun `when get vibrations enabled without a value then return default true`() = runTest {
        // when
        val result = sut.getVibrationsEnabled()

        // then
        assertTrue(result)
    }

    @Test
    fun `when get vibrations enabled with a value then return it`() = runTest {
        // given
        sut.setVibrationsEnabled(false)

        // when
        val result = sut.getVibrationsEnabled()

        // then
        assertFalse(result)
    }

    @Test
    fun `when set vibrations enabled then the value is saved`() = runTest {
        // when
        sut.setVibrationsEnabled(false)
        val result = sut.getVibrationsEnabled()

        // then
        assertFalse(result)
    }
}
