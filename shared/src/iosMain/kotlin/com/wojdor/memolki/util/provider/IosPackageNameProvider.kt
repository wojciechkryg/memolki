package com.wojdor.memolki.util.provider

import platform.Foundation.NSBundle

class IosPackageNameProvider : PackageNameProvider {

    override fun providePackageName(): String =
        NSBundle.mainBundle.bundleIdentifier.orEmpty()
}
