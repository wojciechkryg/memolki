package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.provider.PackageNameProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class GetMoreAppsUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val packageNameProvider: PackageNameProvider
) : BaseUseCase<List<AppModel>>(coroutineDispatcher) {

    override fun execute() = flow {
        val currentAppId = packageNameProvider.providePackageName()
        val otherApps = AppModel.all().filterNot { it.appId == currentAppId }
        emit(Result.success(otherApps))
    }
}
