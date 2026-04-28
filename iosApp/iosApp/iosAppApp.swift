import SwiftUI
import Shared

@main
struct iosAppApp: App {
    init() {
        InitKoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
