package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.notification.NotificationScheduler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShouldShowNotificationRequestUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val notificationScheduler: NotificationScheduler
) : BaseUseCase<Boolean>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Boolean>> {
        return userRepository.getTotalGamesPlayed().map { totalGamesPlayed ->
            val shouldShow = !notificationScheduler.hasNotificationPermission() &&
                    totalGamesPlayed > 0 &&
                    totalGamesPlayed % NOTIFICATION_REQUEST_GAME_INTERVAL == 1L
            Result.success(shouldShow)
        }
    }

    companion object {
        private const val NOTIFICATION_REQUEST_GAME_INTERVAL = 3
    }
}
