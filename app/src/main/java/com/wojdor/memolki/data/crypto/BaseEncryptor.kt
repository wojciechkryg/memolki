package com.wojdor.memolki.data.crypto

import android.util.Base64
import com.wojdor.memolki.data.crypto.EncryptorKeyStore.Companion.TRANSFORMATION
import com.wojdor.memolki.util.extension.logE
import java.nio.ByteBuffer
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class BaseEncryptor @Inject constructor(
    private val keyStore: EncryptorKeyStore
) : Encryptor {

    override fun encrypt(value: Long): String {
        try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, keyStore.secretKey)
            val iv = cipher.iv
            val valueBytes = ByteBuffer.allocate(Long.SIZE_BYTES).putLong(value).array()
            val encryptedBytes = cipher.doFinal(valueBytes)
            val combined = iv + encryptedBytes
            return Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (error: Exception) {
            val message = "Encryption error"
            logE(message, error)
            throw IllegalStateException(message, error)
        }
    }

    override fun decrypt(encryptedValue: String): Long {
        try {
            val combined = Base64.decode(encryptedValue, Base64.DEFAULT)
            val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
            val encryptedBytes = combined.copyOfRange(GCM_IV_LENGTH, combined.size)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, keyStore.secretKey, spec)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            return ByteBuffer.wrap(decryptedBytes).long
        } catch (error: Exception) {
            val message = "Decryption error"
            logE(message, error)
            throw IllegalStateException(message, error)
        }
    }

    companion object {
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 128
    }
}
