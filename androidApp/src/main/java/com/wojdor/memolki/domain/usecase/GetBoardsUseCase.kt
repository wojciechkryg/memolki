package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map

class GetBoardsUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase
) : BaseUseCase<List<BoardModel>>(coroutineDispatcher) {

    override fun execute() =
        getUnlockedCardPairsCountUseCase().map { result ->
            result.map { unlockedCardPairsCount ->
                prepareBoards(unlockedCardPairsCount)
            }
        }

    private fun prepareBoards(unlockedCardPairsCount: Int): List<BoardModel> {
        return listOf(
            unlockBoardIfNeeded(BoardModel.Grid2x3(), unlockedCardPairsCount),
            unlockBoardIfNeeded(BoardModel.Grid3x4(), unlockedCardPairsCount),
            unlockBoardIfNeeded(BoardModel.Grid4x4(), unlockedCardPairsCount),
            unlockBoardIfNeeded(BoardModel.Grid4x5(), unlockedCardPairsCount),
            unlockBoardIfNeeded(BoardModel.Grid4x6(), unlockedCardPairsCount),
            unlockBoardIfNeeded(BoardModel.Grid5x6(), unlockedCardPairsCount)
        )
    }

    private fun unlockBoardIfNeeded(board: BoardModel, unlockedCardPairsCount: Int): BoardModel {
        val requiredCardPairsCount = (board.columns * board.rows) / 2
        return if (unlockedCardPairsCount >= requiredCardPairsCount) {
            when (board) {
                BoardModel.Empty -> board
                is BoardModel.Grid2x3 -> board.copy(isUnlocked = true)
                is BoardModel.Grid3x4 -> board.copy(isUnlocked = true)
                is BoardModel.Grid4x4 -> board.copy(isUnlocked = true)
                is BoardModel.Grid4x5 -> board.copy(isUnlocked = true)
                is BoardModel.Grid4x6 -> board.copy(isUnlocked = true)
                is BoardModel.Grid5x6 -> board.copy(isUnlocked = true)
            }
        } else {
            board
        }
    }
}
