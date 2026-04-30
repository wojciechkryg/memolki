package com.wojdor.memolki.util.extension

import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

internal fun topViewController(): UIViewController? {
    val scene = UIApplication.sharedApplication.connectedScenes
        .firstOrNull { it is UIWindowScene } as? UIWindowScene
    val window = scene?.windows?.firstOrNull { (it as? UIWindow)?.keyWindow == true } as? UIWindow
        ?: scene?.windows?.firstOrNull() as? UIWindow
    var top = window?.rootViewController
    while (top?.presentedViewController != null) {
        top = top.presentedViewController
    }
    return top
}
