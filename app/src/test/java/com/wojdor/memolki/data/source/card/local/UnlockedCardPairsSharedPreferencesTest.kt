package com.wojdor.memolki.data.source.card.local

import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UnlockedCardPairsSharedPreferencesTest : AppTest() {

    private val sharedPreferences = MockSharedPreferences()

    private lateinit var sut: UnlockedCardPairsLocalDataSource

    @Before
    override fun setup() {
        super.setup()
        sut = UnlockedCardPairsLocalDataSource(
            sharedPreferences,
            MockAllCardPairsDataSource
        )
    }

    @Test
    fun `when get unlocked card pair ids without them then return default unlocked card pair ids`() {
        // when
        val result = sut.getUnlockedCardPairIds()

        // then
        val expected = setOf(
            "banana",
            "apple",
            "strawberry",
            "orange",
            "grape",
            "watermelon",
            "mango",
            "peach",
            "pineapple",
            "blueberry"
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when get unlocked card pair ids with already unlocked card pair ids then return them`() {
        // given
        sharedPreferences.edit().putStringSet(
            "unlocked_card_pairs", setOf(
                "banana",
                "apple",
                "strawberry",
                "orange",
                "grape",
                "watermelon",
                "mango",
                "peach",
                "pineapple",
                "blueberry",
                "lemon"
            )
        ).apply()

        // when
        val result = sut.getUnlockedCardPairIds()

        // Then
        val expected = setOf(
            "banana",
            "apple",
            "strawberry",
            "orange",
            "grape",
            "watermelon",
            "mango",
            "peach",
            "pineapple",
            "blueberry",
            "lemon"
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when add unlocked card pair then new card pair id is added to data source`() {
        // given
        val newUnlockedPairId = "lemon"

        // when
        sut.addUnlockedCardPairId(newUnlockedPairId)
        val result = sut.getUnlockedCardPairIds()

        // then
        assertEquals(11, result.size)
        assertTrue(result.contains("lemon"))
    }
}
