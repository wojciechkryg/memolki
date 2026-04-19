package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBiggestUnlockedBoardUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val getBoardsUseCase: GetBoardsUseCase
) : BaseParameterUseCase<String, BoardModel>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(biggestBoardId: String) =
        getBoardsUseCase().map { result ->
            result.map { boards ->
                getBiggestUnlockedBoard(boards, biggestBoardId)
            }
        }

    private fun getBiggestUnlockedBoard(
        boards: List<BoardModel>,
        biggestBoardId: String
    ): BoardModel {
        val unlockedBoards = boards.filter { it.isUnlocked }
        val requestedBoard = boards.find { it.id == biggestBoardId }
        return when {
            requestedBoard?.isUnlocked == true -> requestedBoard
            unlockedBoards.isNotEmpty() -> unlockedBoards.last()
            else -> boards.first()
        }
    }
}
