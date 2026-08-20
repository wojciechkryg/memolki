import Foundation
import GoogleMobileAds
import Shared
import UIKit

final class RewardedAdsBridge: NSObject, IosRewardedAdsBridge {

    private var loadedAds: [String: GADRewardedAd] = [:]
    private var onDismissHandler: (() -> Void)?

    func load(adUnitId: String, completion: @escaping (KotlinBoolean) -> Void) {
        GADRewardedAd.load(withAdUnitID: adUnitId, request: GADRequest()) { [weak self] ad, error in
            if let ad = ad, error == nil {
                self?.loadedAds[adUnitId] = ad
                completion(KotlinBoolean(bool: true))
            } else {
                self?.loadedAds[adUnitId] = nil
                completion(KotlinBoolean(bool: false))
            }
        }
    }

    func isLoaded(adUnitId: String) -> Bool {
        loadedAds[adUnitId] != nil
    }

    func show(adUnitId: String, onReward: @escaping () -> Void, onDismiss: @escaping () -> Void) {
        guard let ad = loadedAds[adUnitId], let root = Self.rootViewController() else {
            onDismiss()
            return
        }
        loadedAds[adUnitId] = nil
        onDismissHandler = onDismiss
        ad.fullScreenContentDelegate = self
        ad.present(fromRootViewController: root) {
            onReward()
        }
    }

    private static func rootViewController() -> UIViewController? {
        let scene = UIApplication.shared.connectedScenes
            .first { $0.activationState == .foregroundActive } as? UIWindowScene
            ?? UIApplication.shared.connectedScenes.first as? UIWindowScene
        var top = scene?.keyWindow?.rootViewController ?? scene?.windows.first?.rootViewController
        while let presented = top?.presentedViewController {
            top = presented
        }
        return top
    }
}

extension RewardedAdsBridge: GADFullScreenContentDelegate {
    func adDidDismissFullScreenContent(_ ad: GADFullScreenPresentingAd) {
        onDismissHandler?()
        onDismissHandler = nil
    }

    func ad(_ ad: GADFullScreenPresentingAd, didFailToPresentFullScreenContentWithError error: Error) {
        onDismissHandler?()
        onDismissHandler = nil
    }
}
