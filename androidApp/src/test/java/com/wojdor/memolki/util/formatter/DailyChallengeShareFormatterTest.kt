package com.wojdor.memolki.util.formatter

import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakePackageNameProvider
import com.wojdor.memolki.util.provider.TimeProvider
import com.wojdor.memolki.util.resource.StringProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class DailyChallengeShareFormatterTest : AppTest() {

    private val packageNameProvider: FakePackageNameProvider by inject()
    private val timeProvider: TimeProvider by inject()
    private val timeFormatter: TimeFormatter by inject()
    private val stringProvider: StringProvider by inject()

    private lateinit var sut: DailyChallengeShareFormatter

    @Before
    override fun setup() {
        super.setup()
        sut = DailyChallengeShareFormatter(packageNameProvider, timeProvider, timeFormatter, stringProvider)
    }

    @Test
    fun `when star count is 3 then three stars emoji is used`() {
        // given
        val result = createModel(starCount = 3)

        // when
        val text = format(result)

        // then
        assertTrue(text.contains("⭐⭐⭐"))
    }

    @Test
    fun `when star count is 2 then two stars emoji is used`() {
        // given
        val result = createModel(starCount = 2)

        // when
        val text = format(result)

        // then
        assertTrue(text.contains("⭐⭐"))
        assertFalse(text.contains("⭐⭐⭐"))
    }

    @Test
    fun `when star count is 1 then one star emoji is used`() {
        // given
        val result = createModel(starCount = 1)

        // when
        val text = format(result)

        // then
        assertTrue(text.contains("⭐"))
        assertFalse(text.contains("⭐⭐"))
    }

    @Test
    fun `when grid has perfect and non-perfect flips then output has green and red squares`() {
        // given
        val result = createModel(cardFlipCounts = listOf(listOf(1, 3)))

        // when
        val text = format(result)

        // then
        assertTrue(text.contains("🟩"))
        assertTrue(text.contains("🟥"))
    }

    @Test
    fun `when all flips are perfect then grid has only green squares`() {
        // given
        val result = createModel(cardFlipCounts = listOf(listOf(1, 2), listOf(2, 1)))

        // when
        val text = format(result)

        // then
        assertTrue(text.contains("🟩"))
        assertFalse(text.contains("🟥"))
    }

    @Test
    fun `when no flips are perfect then grid has only red squares`() {
        // given
        val result = createModel(cardFlipCounts = listOf(listOf(3, 4), listOf(5, 3)))

        // when
        val text = format(result)

        // then
        assertTrue(text.contains("🟥"))
        assertFalse(text.contains("🟩"))
    }

    @Test
    fun `when multi-row grid then rows are separated by newlines`() {
        // given
        val result = createModel(
            cardFlipCounts = listOf(listOf(1, 1), listOf(3, 3))
        )

        // when
        val text = format(result)

        // then
        assertTrue(text.contains("🟩🟩\n🟥🟥"))
    }

    @Test
    fun `when date formatted then slashes are replaced with unicode division slash`() {
        // given
        val result = createModel(epochDay = 100L)

        // when
        val text = format(result)

        // then
        assertTrue(text.contains("\u2215"))
        val dateLine = text.lines().first()
        assertFalse(dateLine.contains("/"))
    }

    @Test
    fun `when time formatted then colons are replaced with unicode ratio`() {
        // given
        val result = createModel(timeMillis = 65_000L)

        // when
        val text = format(result)

        // then
        val timeLine = text.lines()[1]
        assertTrue(timeLine.contains("\u2236"))
        assertFalse(timeLine.contains(":"))
    }

    @Test
    fun `when time formatted then dot is replaced with unicode one dot leader`() {
        // given
        val result = createModel(timeMillis = 5_123L)

        // when
        val text = format(result)

        // then
        val timeLine = text.lines()[1]
        assertTrue(timeLine.contains("\u2024"))
        assertFalse(timeLine.contains("."))
    }

    @Test
    fun `when formatted then output contains all expected sections`() {
        // given
        val result = createModel(
            starCount = 2,
            mistakeCount = 2,
            timeMillis = 65_123L,
            epochDay = 100L,
            cardFlipCounts = listOf(listOf(1, 3))
        )

        // when
        val text = format(result)

        // then
        val lines = text.lines()
        assertTrue(lines[0].startsWith("🃏 $TEST_APP_NAME"))
        assertTrue(lines[1].startsWith("⏱️"))
        assertTrue(lines[2].contains("⭐"))
        assertTrue(lines[2].contains(TEST_MISTAKE_TEXT))
        assertTrue(lines[3].contains("🟩") || lines[3].contains("🟥"))
        assertTrue(lines.last().contains("play.google.com"))
    }

    @Test
    fun `when formatted then store link contains package name`() {
        // given
        val result = createModel()

        // when
        val text = format(result)

        // then
        assertTrue(text.contains("https://play.google.com/store/apps/details?id=${packageNameProvider.mockPackageName}"))
    }

    @Test
    fun `when formatted then date line contains app name and date`() {
        // given
        val result = createModel(epochDay = 20088L)

        // when
        val text = format(result)

        // then
        val dateLine = text.lines().first()
        assertTrue(dateLine.contains(TEST_APP_NAME))
        assertTrue(dateLine.contains("31"))
        assertTrue(dateLine.contains("12"))
        assertTrue(dateLine.contains("2024"))
    }

    private fun format(result: DailyChallengeModel): String = sut.formatText(
        result = result,
        appName = TEST_APP_NAME,
        mistakeText = TEST_MISTAKE_TEXT,
        packageName = packageNameProvider.mockPackageName
    )

    private fun createModel(
        starCount: Int = 2,
        mistakeCount: Int = 2,
        timeMillis: Long = 60_000L,
        epochDay: Long = 0L,
        cardFlipCounts: List<List<Int>> = listOf(listOf(1, 1))
    ) = DailyChallengeModel(
        starCount = starCount,
        mistakeCount = mistakeCount,
        timeMillis = timeMillis,
        epochDay = epochDay,
        cardFlipCounts = cardFlipCounts
    )

    companion object {
        private const val TEST_APP_NAME = "TestApp"
        private const val TEST_MISTAKE_TEXT = "2 mistakes"
    }
}
