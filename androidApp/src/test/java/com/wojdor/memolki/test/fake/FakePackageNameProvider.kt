package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.PackageNameProvider

class FakePackageNameProvider : PackageNameProvider {

    var mockPackageName: String = "com.wojdor.memolki.fruithalf"

    override fun providePackageName(): String {
        return mockPackageName
    }
}
