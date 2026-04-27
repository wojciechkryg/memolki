package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakePackageNameProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetMoreAppsUseCaseTest : AppTest() {

    private val packageNameProvider: FakePackageNameProvider by inject()
    private lateinit var sut: GetMoreAppsUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when current app is fruitHalf then return all other apps`() = runTest {
        // given
        val currentAppId = AppModel.FruitHalf.appId
        packageNameProvider.mockPackageName = currentAppId

        // when
        val result = sut().first()

        // then
        val expected = AppModel.all().filter { it.appId != currentAppId }
        assertEquals(Result.success(expected), result)
    }
}
