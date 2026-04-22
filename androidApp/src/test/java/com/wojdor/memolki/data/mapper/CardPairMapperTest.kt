package com.wojdor.memolki.data.mapper

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.empty

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
            pair = CardEntity.Image("id", Res.string.empty, 321) to
                    CardEntity.Text("id", Res.string.empty),
            addedEpochDay = 20439L
        )

        // when
        val result = sut.toModel()

        // then
        val expected = CardPairModel(
            CardModel.Image("id", "pairId", Res.string.empty, 321),
            CardModel.Text("id", "pairId", Res.string.empty),
            addedEpochDay = 20439L
        )
        assertEquals(expected, result)
    }
}
