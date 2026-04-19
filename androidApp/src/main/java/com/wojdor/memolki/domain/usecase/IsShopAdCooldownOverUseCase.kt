package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.notification.NotificationScheduler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map

class IsShopAdCooldownOverUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseUseCase<Boolean>(coroutineDispatcher) {

    override fun execute() =
        userRepository.getLastShopAdShownTimestamp().map { lastShown ->
            Result.success(System.currentTimeMillis() - lastShown >= NotificationScheduler.SHOP_AD_COOLDOWN_MS)
        }
}
