package com.wojdor.memolki.ui.feature.collection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.app.navigateToCardPairDetailsScreen
import com.wojdor.memolki.ui.app.navigateToEnableNotifications
import com.wojdor.memolki.ui.app.navigateToShop
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsIntent
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsViewModel
import com.wojdor.memolki.ui.feature.collection.component.CollectionContent
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationDestination
import com.wojdor.memolki.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = koinViewModel(),
    cardPairDetailsViewModel: CardPairDetailsViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, cardPairDetailsViewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: CollectionViewModel,
    cardPairDetailsViewModel: CardPairDetailsViewModel,
    navController: NavController
) {
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is CollectionEffect.OpenShopScreen -> navController.navigateToShop()
            is CollectionEffect.OpenEnableNotificationsScreen ->
                navController.navigateToEnableNotifications(EnableNotificationDestination.COLLECTION.route)

            is CollectionEffect.OpenCardPairDetailsScreen ->
                openCardPairDetailsScreen(
                    viewModel,
                    cardPairDetailsViewModel,
                    navController,
                    effect.cardPairModel
                )

            is CollectionEffect.ShowAd -> showAd(viewModel, effect.rewardedAd)
        }
    }
}

private fun openCardPairDetailsScreen(
    viewModel: CollectionViewModel,
    cardPairDetailsViewModel: CardPairDetailsViewModel,
    navController: NavController,
    cardPairModel: CardPairModel
) {
    val unlockedCardPairs = viewModel.uiState.value.collectionCardPairs
        .filterIsInstance<CollectionCardPairModel.Unlocked>()
        .map { it.cardPair }
    val initialPage = unlockedCardPairs.indexOf(cardPairModel)
        .takeIf { it != -1 } ?: 0
    cardPairDetailsViewModel.sendIntent(
        CardPairDetailsIntent.OnCardPairDetailsShow(
            unlockedCardPairs,
            initialPage
        )
    )
    navController.navigateToCardPairDetailsScreen()
}

private fun showAd(
    viewModel: CollectionViewModel,
    rewardedAd: RewardedAd
) {
    rewardedAd.show(
        onGrantReward = { viewModel.sendIntent(CollectionIntent.OnAdReward) },
        onAdDismiss = { viewModel.sendIntent(CollectionIntent.OnAdDismiss(it)) }
    )
}

@Composable
private fun HandleState(
    viewModel: CollectionViewModel,
    state: CollectionState
) {
    val callbacks = CollectionCallbacks(
        onShopButtonClick = {
            viewModel.sendIntent(CollectionIntent.OnShopClick)
        },
        onUnlockedCardPairClick = {
            viewModel.sendIntent(CollectionIntent.OnCardPairClick(it))
        },
        onUnlockWithCoinsClick = {
            viewModel.sendIntent(CollectionIntent.OnUnlockWithCoinsClick(it))
        },
        onUnlockWithAdClick = {
            viewModel.sendIntent(CollectionIntent.OnUnlockWithAdClick(it))
        }
    )
    CollectionScreen(state, callbacks)
}

@Composable
fun CollectionScreen(
    state: CollectionState,
    callbacks: CollectionCallbacks = CollectionCallbacks()
) {
    CollectionContent(state, callbacks)
}

@Preview
@Composable
private fun CollectionPreview() {
    AppTheme {
        PreviewBackground {
            CollectionContent(
                state = getCollectionStateForPreview()
            )
        }
    }
}

