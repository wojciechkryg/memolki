import SwiftUI
import Shared
import FirebaseCore
import FirebaseMessaging
import UserNotifications
import GoogleMobileAds
import AppTrackingTransparency

@main
struct iosAppApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    init() {
        InitKoinKt.doInitKoin(
            storeKitBridge: StoreKitBridge(),
            rewardedAdsBridge: RewardedAdsBridge()
        )
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        GADMobileAds.sharedInstance().start(completionHandler: nil)
        guard Bundle.main.path(forResource: "GoogleService-Info", ofType: "plist") != nil else {
            return true
        }
        FirebaseApp.configure()
        Messaging.messaging().delegate = self
        UNUserNotificationCenter.current().delegate = self
        application.registerForRemoteNotifications()
        return true
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        ATTrackingManager.requestTrackingAuthorization { _ in }
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().apnsToken = deviceToken
    }

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {}
}
