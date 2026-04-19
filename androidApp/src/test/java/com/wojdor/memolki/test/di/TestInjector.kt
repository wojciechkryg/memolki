package com.wojdor.memolki.test.di

import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSourceTest
import com.wojdor.memolki.data.local.datastore.notification.NotificationLocalDataSourceTest
import com.wojdor.memolki.data.local.settings.SettingsLocalDataSourceTest
import com.wojdor.memolki.data.local.user.UserLocalDataSourceTest
import com.wojdor.memolki.data.repository.CardRepositoryTest
import com.wojdor.memolki.data.repository.DailyChallengeRepositoryTest
import com.wojdor.memolki.data.repository.NotificationRepositoryTest
import com.wojdor.memolki.data.repository.SettingsRepositoryTest
import com.wojdor.memolki.data.repository.UserRepositoryTest
import com.wojdor.memolki.domain.usecase.CalculateCoinsForShopAdUseCaseTest
import com.wojdor.memolki.domain.usecase.CalculateNextCardPairCostUseCaseTest
import com.wojdor.memolki.domain.usecase.CanUnlockNewCardUseCaseTest
import com.wojdor.memolki.domain.usecase.ChangeLanguageUseCaseTest
import com.wojdor.memolki.domain.usecase.CheckDailyLoginStreakUseCaseTest
import com.wojdor.memolki.domain.usecase.CollectDailyStreakRewardUseCaseTest
import com.wojdor.memolki.domain.usecase.GetAllCardPairsCountUseCaseTest
import com.wojdor.memolki.domain.usecase.GetAllCardPairsUseCaseTest
import com.wojdor.memolki.domain.usecase.GetAllDailyChallengesUseCaseTest
import com.wojdor.memolki.domain.usecase.GetBiggestUnlockedBoardUseCaseTest
import com.wojdor.memolki.domain.usecase.GetBoardsUseCaseTest
import com.wojdor.memolki.domain.usecase.GetCoinsUseCaseTest
import com.wojdor.memolki.domain.usecase.GetCollectionDataUseCaseTest
import com.wojdor.memolki.domain.usecase.GetCurrentLanguageTagUseCaseTest
import com.wojdor.memolki.domain.usecase.GetDailyChallengeCardsUseCaseTest
import com.wojdor.memolki.domain.usecase.GetLanguagesWithCurrentUseCaseTest
import com.wojdor.memolki.domain.usecase.GetLevelUseCaseTest
import com.wojdor.memolki.domain.usecase.GetMenuUseCaseTest
import com.wojdor.memolki.domain.usecase.GetMoreAppsUseCaseTest
import com.wojdor.memolki.domain.usecase.GetSettingsUseCaseTest
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCaseTest
import com.wojdor.memolki.domain.usecase.GetSupportedLanguagesUseCaseTest
import com.wojdor.memolki.domain.usecase.GetTodayDailyChallengeUseCaseTest
import com.wojdor.memolki.domain.usecase.GetTotalCardPairsMatchedUseCaseTest
import com.wojdor.memolki.domain.usecase.GetTotalCoinsUseCaseTest
import com.wojdor.memolki.domain.usecase.GetTotalGamesPlayedUseCaseTest
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsCountUseCaseTest
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsFromAdsCountUseCaseTest
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsUseCaseTest
import com.wojdor.memolki.domain.usecase.HasAnyDailyChallengeUseCaseTest
import com.wojdor.memolki.domain.usecase.HasNotPlayedAnyGameUseCaseTest
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCaseTest
import com.wojdor.memolki.domain.usecase.HasReceivedShareRewardUseCaseTest
import com.wojdor.memolki.domain.usecase.IncrementLevelUseCaseTest
import com.wojdor.memolki.domain.usecase.IncrementTotalCardPairsMatchedUseCaseTest
import com.wojdor.memolki.domain.usecase.IncrementTotalGamesPlayedUseCaseTest
import com.wojdor.memolki.domain.usecase.IncrementUnlockedCardPairsFromAdsCountUseCaseTest
import com.wojdor.memolki.domain.usecase.IsAppInstalledUseCaseTest
import com.wojdor.memolki.domain.usecase.IsShopAdCooldownOverUseCaseTest
import com.wojdor.memolki.domain.usecase.ObserveMusicEnabledUseCaseTest
import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCaseTest
import com.wojdor.memolki.domain.usecase.PrepareRecordingDataUseCaseTest
import com.wojdor.memolki.domain.usecase.RewardCoinsForBoardUseCaseTest
import com.wojdor.memolki.domain.usecase.RewardCoinsForShareUseCaseTest
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopAdUseCaseTest
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopPurchaseUseCaseTest
import com.wojdor.memolki.domain.usecase.SaveDailyChallengeUseCaseTest
import com.wojdor.memolki.domain.usecase.ScheduleAdRewardNotificationUseCaseTest
import com.wojdor.memolki.domain.usecase.SetLastShopAdShownTimestampUseCaseTest
import com.wojdor.memolki.domain.usecase.ShouldShowNotificationRequestUseCaseTest
import com.wojdor.memolki.domain.usecase.ToggleSettingsUseCaseTest
import com.wojdor.memolki.domain.usecase.UnlockAllCardPairsUseCaseTest
import com.wojdor.memolki.domain.usecase.UnlockAllNewCardPairsIfPurchasedUseCaseTest
import com.wojdor.memolki.domain.usecase.UnlockRandomCardIfEnoughCoinsUseCaseTest
import com.wojdor.memolki.domain.usecase.UnlockRandomCardUseCaseTest
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.ui.app.AppViewModelTest
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsViewModelTest
import com.wojdor.memolki.ui.feature.changelanguage.ChangeLanguageViewModelTest
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardViewModelTest
import com.wojdor.memolki.ui.feature.collection.CollectionViewModelTest
import com.wojdor.memolki.ui.feature.dailychallengehistory.DailyChallengeHistoryViewModelTest
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationsViewModelTest
import com.wojdor.memolki.ui.feature.endgame.EndGameViewModelTest
import com.wojdor.memolki.ui.feature.game.GameViewModelTest
import com.wojdor.memolki.ui.feature.menu.MenuViewModelTest
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsViewModelTest
import com.wojdor.memolki.ui.feature.settings.SettingsViewModelTest
import com.wojdor.memolki.ui.feature.shop.ShopViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
interface TestInjector {
    fun inject(test: AppTest)
    fun inject(test: UnlockedCardPairsLocalDataSourceTest)
    fun inject(test: SettingsLocalDataSourceTest)
    fun inject(test: UserLocalDataSourceTest)
    fun inject(test: CardRepositoryTest)
    fun inject(test: DailyChallengeRepositoryTest)
    fun inject(test: SettingsRepositoryTest)
    fun inject(test: UserRepositoryTest)
    fun inject(test: ShopViewModelTest)
    fun inject(test: ChooseBoardViewModelTest)
    fun inject(test: CalculateNextCardPairCostUseCaseTest)
    fun inject(test: GetAllCardPairsCountUseCaseTest)
    fun inject(test: GetAllCardPairsUseCaseTest)
    fun inject(test: GetCoinsUseCaseTest)
    fun inject(test: GetBoardsUseCaseTest)
    fun inject(test: GetMenuUseCaseTest)
    fun inject(test: GetSettingsUseCaseTest)
    fun inject(test: GetShuffledUnlockedCardsUseCaseTest)
    fun inject(test: GetUnlockedCardPairsCountUseCaseTest)
    fun inject(test: GetUnlockedCardPairsFromAdsCountUseCaseTest)
    fun inject(test: GetUnlockedCardPairsUseCaseTest)
    fun inject(test: IncrementTotalCardPairsMatchedUseCaseTest)
    fun inject(test: IncrementTotalGamesPlayedUseCaseTest)
    fun inject(test: IncrementUnlockedCardPairsFromAdsCountUseCaseTest)
    fun inject(test: RewardCoinsForBoardUseCaseTest)
    fun inject(test: RewardCoinsForShopAdUseCaseTest)
    fun inject(test: RewardCoinsForShopPurchaseUseCaseTest)
    fun inject(test: ToggleSettingsUseCaseTest)
    fun inject(test: UnlockRandomCardIfEnoughCoinsUseCaseTest)
    fun inject(test: UnlockRandomCardUseCaseTest)
    fun inject(test: CardPairDetailsViewModelTest)
    fun inject(test: CollectionViewModelTest)
    fun inject(test: EndGameViewModelTest)
    fun inject(test: GameViewModelTest)
    fun inject(test: MenuViewModelTest)
    fun inject(test: SettingsViewModelTest)
    fun inject(test: UnlockAllCardPairsUseCaseTest)
    fun inject(test: UnlockAllNewCardPairsIfPurchasedUseCaseTest)
    fun inject(test: CalculateCoinsForShopAdUseCaseTest)
    fun inject(test: CanUnlockNewCardUseCaseTest)
    fun inject(test: GetMoreAppsUseCaseTest)
    fun inject(test: GetTotalGamesPlayedUseCaseTest)
    fun inject(test: MoreAppsViewModelTest)
    fun inject(test: IsAppInstalledUseCaseTest)
    fun inject(test: GetSupportedLanguagesUseCaseTest)
    fun inject(test: GetCurrentLanguageTagUseCaseTest)
    fun inject(test: ChangeLanguageUseCaseTest)
    fun inject(test: ChangeLanguageViewModelTest)
    fun inject(test: GetLanguagesWithCurrentUseCaseTest)
    fun inject(test: ScheduleAdRewardNotificationUseCaseTest)
    fun inject(test: ShouldShowNotificationRequestUseCaseTest)
    fun inject(test: EnableNotificationsViewModelTest)
    fun inject(test: GetTotalCoinsUseCaseTest)
    fun inject(test: GetTotalCardPairsMatchedUseCaseTest)
    fun inject(test: SetLastShopAdShownTimestampUseCaseTest)
    fun inject(test: IsShopAdCooldownOverUseCaseTest)
    fun inject(test: HasReceivedShareRewardUseCaseTest)
    fun inject(test: PrepareRecordingDataUseCaseTest)
    fun inject(test: GetBiggestUnlockedBoardUseCaseTest)
    fun inject(test: HasPlayedTodayDailyChallengeUseCaseTest)
    fun inject(test: SaveDailyChallengeUseCaseTest)
    fun inject(test: GetTodayDailyChallengeUseCaseTest)
    fun inject(test: GetCollectionDataUseCaseTest)
    fun inject(test: GetDailyChallengeCardsUseCaseTest)
    fun inject(test: GetLevelUseCaseTest)
    fun inject(test: IncrementLevelUseCaseTest)
    fun inject(test: RewardCoinsForShareUseCaseTest)
    fun inject(test: CollectDailyStreakRewardUseCaseTest)
    fun inject(test: CheckDailyLoginStreakUseCaseTest)
    fun inject(test: AppViewModelTest)
    fun inject(test: NotificationLocalDataSourceTest)
    fun inject(test: NotificationRepositoryTest)
    fun inject(test: GetAllDailyChallengesUseCaseTest)
    fun inject(test: HasAnyDailyChallengeUseCaseTest)
    fun inject(test: HasNotPlayedAnyGameUseCaseTest)
    fun inject(test: DailyChallengeHistoryViewModelTest)
    fun inject(test: ObserveMusicEnabledUseCaseTest)
    fun inject(test: ObserveSoundEnabledUseCaseTest)
}
