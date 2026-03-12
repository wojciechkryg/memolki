package com.wojdor.memolki.data.crypto

interface Encryptor {

    suspend fun encrypt(value: Long): String
    suspend fun decrypt(encryptedValue: String): Long
}
