package com.wojdor.memolki.data.crypto

// TODO(kmp-ios): replace with a Keychain-backed AES key store when iOS persistence ships.
class IosLocalEncryptorKeyStore : LocalEncryptorKeyStore {
    override suspend fun initialize() = Unit
}
