package com.wojdor.memolki.data.mapper

import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.data.entity.CardPairEntity
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CardPairMapperTest : AppTest() {

    @Test
    fun `when map card pair entity then return card pair model`() {
        // given
        val sut = CardPairEntity(
            id = "pairId",
            pair = CardEntity.Image("id", 123, 321) to
                    CardEntity.Text("id", 123)
        )

        // when
        val result = sut.toModel()

        // then
        val expected = CardPairModel(
            CardModel.Image("id", "pairId", 123, 321) to
                    CardModel.Text("id", "pairId", 123)
        )
        assertEquals(expected, result)
    }
}
