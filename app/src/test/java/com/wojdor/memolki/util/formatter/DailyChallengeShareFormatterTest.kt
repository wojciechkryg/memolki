package com.wojdor.memolki.util.formatter

import android.content.Context
import android.content.res.Resources
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.fake.FakeTimeProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DailyChallengeShareFormatterTest {

    private val context: Context = mockk(relaxed = true)
    private val resources: Resources = mockk(relaxed = true)
    private val timeProvider = FakeTimeProvider()
    private val timeFormatter = TimeFormatter()
    private lateinit var sut: DailyChallengeShareFormatter

    @Before
    fun setup() {
        every { context.resources } returns resources
        every { context.getString(any()) } returns "TestApp"
        every { context.packageName } returns "com.test"
        every { resources.getQuantityString(any(), any(), any()) } returns "2 mistakes"
        sut = DailyChallengeShareFormatter(context, timeProvider, timeFormatter)
    }

    @Test
    fun `when star count is 3 then three stars emoji is used`() {
        // given
        val result = DailyChallengeModel(
            starCount = 3,
            mistakeCount = 0,
            timeMillis = 5000L,
            epochDay = 100L,
            cardFlipCounts = listOf(listOf(1, 2))
        )

        // when
        val text = sut.format(result, listOf(listOf(true, true)))

        // then
        assertTrue(text.contains("⭐⭐⭐"))
    }

    @Test
    fun `when star count is 2 then two stars emoji is used`() {
        // given
        val result = DailyChallengeModel(
            starCount = 2,
            mistakeCount = 2,
            timeMillis = 5000L,
            epochDay = 100L,
            cardFlipCounts = listOf(listOf(1, 3))
        )

        // when
        val text = sut.format(result, listOf(listOf(true, false)))

        // then
        assertTrue(text.contains("⭐⭐"))
    }

    @Test
    fun `when star count is 1 then one star emoji is used`() {
        // given
        val result = DailyChallengeModel(
            starCount = 1,
            mistakeCount = 5,
            timeMillis = 5000L,
            epochDay = 100L,
            cardFlipCounts = listOf(listOf(3, 4))
        )

        // when
        val text = sut.format(result, listOf(listOf(false, false)))

        // then
        assertTrue(text.contains("⭐"))
    }

    @Test
    fun `when grid has perfect and non-perfect flips then output has green and red squares`() {
        // given
        val result = DailyChallengeModel(
            starCount = 2,
            mistakeCount = 2,
            timeMillis = 5000L,
            epochDay = 100L,
            cardFlipCounts = listOf(listOf(1, 3))
        )
        val grid = listOf(listOf(true, false))

        // when
        val text = sut.format(result, grid)

        // then
        assertTrue(text.contains("🟩"))
        assertTrue(text.contains("🟥"))
    }
}
