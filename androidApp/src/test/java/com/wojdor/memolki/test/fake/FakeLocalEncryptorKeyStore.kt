package com.wojdor.memolki.test.fake

import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore

class FakeLocalEncryptorKeyStore : LocalEncryptorKeyStore {
    var initializeCount: Int = 0
        private set

    override suspend fun initialize() {
        initializeCount++
    }
}
