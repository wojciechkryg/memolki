package com.wojdor.memolki.data.crypto

import android.util.Base64
import com.wojdor.memolki.util.extension.logE
import java.nio.ByteBuffer
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class BaseEncryptor(
    private val localKeyStore: LocalEncryptorKeyStore
) : Encryptor {

    override suspend fun encrypt(value: Long): String {
        try {
            return encryptWithKey(value, localKeyStore.getSecretKey())
        } catch (error: Exception) {
            val message = "Encryption error"
            logE(message, error)
            throw IllegalStateException(message, error)
        }
    }

    override suspend fun decrypt(encryptedValue: String): Long {
        try {
            return decryptWithKey(encryptedValue, localKeyStore.getSecretKey())
        } catch (error: Exception) {
            val message = "Decryption error"
            logE(message, error)
            throw IllegalStateException(message, error)
        }
    }

    private fun encryptWithKey(value: Long, key: SecretKey): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val valueBytes = ByteBuffer.allocate(Long.SIZE_BYTES).putLong(value).array()
        val encryptedBytes = cipher.doFinal(valueBytes)
        val combined = iv + encryptedBytes
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    private fun decryptWithKey(encryptedValue: String, key: SecretKey): Long {
        val combined = Base64.decode(encryptedValue, Base64.DEFAULT)
        val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
        val encryptedBytes = combined.copyOfRange(GCM_IV_LENGTH, combined.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return ByteBuffer.wrap(decryptedBytes).long
    }

    companion object {
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 128
    }
}
