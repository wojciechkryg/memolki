package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeAppInstalledProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class IsAppInstalledUseCaseTest : AppTest() {

    private val fakeAppInstalledProvider: FakeAppInstalledProvider by inject()
    private lateinit var sut: IsAppInstalledUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when the fruitHalf app is installed then return true`() = runTest {
        // given
        val app = AppModel.FruitHalf
        fakeAppInstalledProvider.mockAppInstalled = true

        // when
        val result = sut(app.appId).first()

        // then
        assertEquals(Result.success(true), result)
    }

    @Test
    fun `when the fruitHalf app is not installed then return false`() = runTest {
        // given
        val app = AppModel.FruitHalf
        fakeAppInstalledProvider.mockAppInstalled = false

        // when
        val result = sut(app.appId).first()

        // then
        assertEquals(Result.success(false), result)
    }
}
