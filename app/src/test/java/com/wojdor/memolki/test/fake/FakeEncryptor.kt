package com.wojdor.memolki.test.fake

import com.wojdor.memolki.data.crypto.Encryptor
import javax.inject.Inject

class FakeEncryptor @Inject constructor() : Encryptor {

    override fun encrypt(value: Long) = value.toString()

    override fun decrypt(encryptedValue: String) = encryptedValue.toLong()
}
