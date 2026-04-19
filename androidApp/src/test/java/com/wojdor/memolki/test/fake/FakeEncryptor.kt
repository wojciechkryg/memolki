package com.wojdor.memolki.test.fake

import com.wojdor.memolki.data.crypto.Encryptor

class FakeEncryptor : Encryptor {

    override suspend fun encrypt(value: Long) = value.toString()

    override suspend fun decrypt(encryptedValue: String) = encryptedValue.toLong()
}
