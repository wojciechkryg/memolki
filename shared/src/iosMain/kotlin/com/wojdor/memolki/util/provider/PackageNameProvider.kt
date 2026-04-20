package com.wojdor.memolki.util.provider

import platform.Foundation.NSBundle

actual open class PackageNameProvider {

    actual open fun providePackageName(): String =
        NSBundle.mainBundle.bundleIdentifier.orEmpty()
}
