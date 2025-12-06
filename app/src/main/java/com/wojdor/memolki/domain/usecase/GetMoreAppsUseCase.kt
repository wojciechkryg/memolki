package com.wojdor.memolki.domain.usecase

import android.util.Log
import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.provider.PackageNameProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMoreAppsUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val packageNameProvider: PackageNameProvider
) : BaseUseCase<List<AppModel>>(coroutineDispatcher) {

    override fun execute() = flow {
        val currentAppId = packageNameProvider.providePackageName()
        val otherApps = AppModel.all().filterNot { it.appId == currentAppId }
        Log.d("TESTWOJDOR", "otherApps: ${otherApps.map { it.appId }}")
        emit(Result.success(otherApps))
    }
}
