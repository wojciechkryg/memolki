package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.PackageNameProvider
import io.mockk.mockk
import javax.inject.Inject

class FakePackageNameProvider @Inject constructor() : PackageNameProvider(mockk()) {

    var mockPackageName: String = "com.wojdor.memolki.fruithalf"

    override fun providePackageName(): String {
        return mockPackageName
    }
}
