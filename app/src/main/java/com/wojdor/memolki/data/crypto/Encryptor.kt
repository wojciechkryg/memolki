package com.wojdor.memolki.data.crypto

interface Encryptor {

    fun encrypt(value: Long): String
    fun decrypt(encryptedValue: String): Long
}
