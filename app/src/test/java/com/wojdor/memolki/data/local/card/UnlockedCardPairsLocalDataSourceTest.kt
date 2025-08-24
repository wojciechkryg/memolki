package com.wojdor.memolki.data.local.card

import androidx.datastore.preferences.core.edit
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UnlockedCardPairsLocalDataSourceTest : AppTest() {

    private val dataStore = MockDataStore()

    private lateinit var sut: UnlockedCardPairsLocalDataSource

    @Before
    override fun setup() {
        super.setup()
        sut = UnlockedCardPairsLocalDataSource(
            dataStore,
            MockAllCardPairsDataSource
        )
    }

    @Test
    fun `when get unlocked card pair ids without them then return default unlocked card pair ids`() =
        runTest {
            // when
            val result = sut.getUnlockedCardPairIds()

            // then
            val expected = listOf(
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
            Assert.assertEquals(expected, result)
        }

    @Test
    fun `when get unlocked card pair ids with already unlocked card pair ids then return them`() =
        runTest {
            // given
            dataStore.edit {
                it[UnlockedCardPairsLocalDataSource.Key.UNLOCKED_CARD_PAIR_IDS] = setOf(
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
            }

            // when
            val result = sut.getUnlockedCardPairIds()

            // Then
            val expected = listOf(
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
            Assert.assertEquals(expected, result)
        }

    @Test
    fun `when add unlocked card pair then new card pair id is added to data source`() = runTest {
        // given
        val newUnlockedPairId = "lemon"

        // when
        sut.addUnlockedCardPairId(newUnlockedPairId)
        val result = sut.getUnlockedCardPairIds()

        // then
        Assert.assertEquals(11, result.size)
        Assert.assertTrue(result.contains("lemon"))
    }
}
