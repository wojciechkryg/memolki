package com.wojdor.memolki.data.crypto

// TODO(kmp-ios): replace with a CryptoKit-backed AES/GCM impl when iOS persistence ships.
class IosEncryptor : Encryptor {
    override suspend fun encrypt(value: Long): String = value.toString()
    override suspend fun decrypt(encryptedValue: String): Long = encryptedValue.toLong()
}
