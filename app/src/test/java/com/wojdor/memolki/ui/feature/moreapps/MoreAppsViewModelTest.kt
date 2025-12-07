package com.wojdor.memolki.ui.feature.moreapps

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.usecase.GetMoreAppsUseCase
import com.wojdor.memolki.domain.usecase.IsAppInstalledUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeAppInstalledProvider
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsEffect.OpenApp
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsEffect.ShowAppInstall
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsIntent.OnAppClick
import com.wojdor.memolki.util.media.HapticFeedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MoreAppsViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @Inject
    lateinit var getMoreAppsUseCase: GetMoreAppsUseCase

    @Inject
    lateinit var fakeAppInstalledProvider: FakeAppInstalledProvider

    private lateinit var isAppInstalledUseCase: IsAppInstalledUseCase

    private lateinit var sut: MoreAppsViewModel

    @Before
    override fun setup() {
        super.setup()
        isAppInstalledUseCase = IsAppInstalledUseCase(
            testDispatcher,
            fakeAppInstalledProvider
        )
        sut = MoreAppsViewModel(
            savedStateHandle,
            hapticFeedback,
            getMoreAppsUseCase,
            isAppInstalledUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when initial load is done then the state is updated with menu`() = runTest {
        sut.uiState.test {
            // given
            skipItems(1)

            // when
            val state = awaitItem()

            // then
            assertEquals(AppModel.VegetableHalf, state.apps[0])
            assertEquals(AppModel.MammalSide, state.apps[1])
        }
    }

    @Test
    fun `when OnAppClick intent is send then the ShowAppInstall effect is send`() = runTest {
        // given
        val app = AppModel.FruitHalf
        fakeAppInstalledProvider.mockAppInstalled = false

        sut.uiEffect.test {
            // when
            sut.sendIntent(OnAppClick(app))

            // then
            assertEquals(ShowAppInstall(app), awaitItem())
        }
    }

    @Test
    fun `when OnAppClick intent is send then the OpenApp effect is send`() = runTest {
        // given
        val app = AppModel.FruitHalf
        fakeAppInstalledProvider.mockAppInstalled = true

        sut.uiEffect.test {
            // when
            sut.sendIntent(OnAppClick(app))

            // then
            assertEquals(OpenApp(app), awaitItem())
        }
    }
}
