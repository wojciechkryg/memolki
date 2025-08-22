package com.wojdor.memolki.data.mapper

import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.domain.model.CardModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CardMapperTest : AppTest() {

    @Test
    fun `when map card entity image then return card model image`() {
        // given
        val sut = CardEntity.Image("id", 123, 321)

        // when
        val result = sut.toModel("pairId")

        // then
        val expected = CardModel.Image("pairId", 123, 321)
        assertEquals(expected, result)
    }

    @Test
    fun `when map card entity text then return card model text`() {
        // given
        val sut = CardEntity.Text("id", 123)

        // when
        val result = sut.toModel("pairId")

        // then
        val expected = CardModel.Text("pairId", 123)
        assertEquals(expected, result)
    }
}
