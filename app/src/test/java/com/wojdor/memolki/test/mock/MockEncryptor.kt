package com.wojdor.memolki.test.mock

import com.wojdor.memolki.data.crypto.Encryptor

class MockEncryptor : Encryptor {

    override fun encrypt(value: Long) = value.toString()

    override fun decrypt(encryptedValue: String) = encryptedValue.toLong()
}
