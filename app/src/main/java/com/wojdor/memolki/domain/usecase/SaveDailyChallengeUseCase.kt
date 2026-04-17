package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.data.repository.NotificationRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveDailyChallengeUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationScheduler: NotificationScheduler,
    private val timeProvider: TimeProvider
) : BaseParameterUseCase<DailyChallengeModel, Unit>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(result: DailyChallengeModel) = flow {
        val epochDay = timeProvider.currentLocalDate().toEpochDay()
        dailyChallengeRepository.saveResult(epochDay, result)
        val nextNotificationTimestamp =
            notificationScheduler.calculateNextDailyChallengeNotificationTimestamp()
        notificationRepository.setNextDailyChallengeNotificationTimestamp(nextNotificationTimestamp)
        notificationScheduler.scheduleDailyChallengeNotification(nextNotificationTimestamp)
        emit(Result.success(Unit))
    }
}
