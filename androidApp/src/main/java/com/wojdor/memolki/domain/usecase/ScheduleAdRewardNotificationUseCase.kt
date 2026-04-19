package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.notification.NotificationScheduler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ScheduleAdRewardNotificationUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val notificationScheduler: NotificationScheduler
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute() = flow {
        notificationScheduler.scheduleAdRewardNotification()
        emit(Result.success(Unit))
    }
}
