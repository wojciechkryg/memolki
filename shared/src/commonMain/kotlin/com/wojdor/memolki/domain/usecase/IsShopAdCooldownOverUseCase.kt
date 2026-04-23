package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.notification.NotificationScheduler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class IsShopAdCooldownOverUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseUseCase<Boolean>(coroutineDispatcher) {

    override fun execute() =
        userRepository.getLastShopAdShownTimestamp().map { lastShown ->
            val now = Clock.System.now().toEpochMilliseconds()
            Result.success(now - lastShown >= NotificationScheduler.SHOP_AD_COOLDOWN_MS)
        }
}
