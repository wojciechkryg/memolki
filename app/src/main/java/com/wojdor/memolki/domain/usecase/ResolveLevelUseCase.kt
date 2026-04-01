package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ResolveLevelUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val getLevelsUseCase: GetLevelsUseCase
) : BaseParameterUseCase<String, LevelModel>(coroutineDispatcher) {

    override fun execute(parameter: String) =
        getLevelsUseCase().map { result ->
            result.map { levels ->
                resolveLevel(levels, parameter)
            }
        }

    private fun resolveLevel(levels: List<LevelModel>, requestedLevelId: String): LevelModel {
        val unlockedLevels = levels.filter { it.isUnlocked }
        val requestedLevel = levels.find { it.id == requestedLevelId }
        return when {
            requestedLevel?.isUnlocked == true -> requestedLevel
            unlockedLevels.isNotEmpty() -> unlockedLevels.last()
            else -> levels.first()
        }
    }
}
