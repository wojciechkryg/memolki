package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.PackageNameProvider
import io.mockk.mockk

class FakePackageNameProvider : PackageNameProvider(mockk()) {

    var mockPackageName: String = "com.wojdor.memolki.fruithalf"

    override fun providePackageName(): String {
        return mockPackageName
    }
}
