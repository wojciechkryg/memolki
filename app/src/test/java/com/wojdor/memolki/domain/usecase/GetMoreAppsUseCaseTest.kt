package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.util.provider.PackageNameProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMoreAppsUseCaseTest : AppTest() {

    private val packageNameProvider: PackageNameProvider = mockk()
    private lateinit var sut: GetMoreAppsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetMoreAppsUseCase(testDispatcher, packageNameProvider)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when current app is fruitHalf then return all other apps`() = runTest {
        // given
        val currentAppId = AppModel.FruitHalf.appId
        every { packageNameProvider.providePackageName() } returns currentAppId

        // when
        val result = sut().first()

        // then
        val expected = AppModel.all().filter { it.appId != currentAppId }
        assertEquals(Result.success(expected), result)
    }
}
