package com.wojdor.memolki.util.provider

expect open class PackageNameProvider {
    open fun providePackageName(): String
}
