package com.wojdor.memolki.data.crypto

import com.wojdor.memolki.util.extension.logE
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreCrypto.CCCrypt
import platform.CoreCrypto.CCOperation
import platform.CoreCrypto.kCCAlgorithmAES
import platform.CoreCrypto.kCCBlockSizeAES128
import platform.CoreCrypto.kCCDecrypt
import platform.CoreCrypto.kCCEncrypt
import platform.CoreCrypto.kCCOptionPKCS7Padding
import platform.CoreCrypto.kCCSuccess
import platform.posix.size_tVar

@OptIn(ExperimentalForeignApi::class)
class IosEncryptor(
    private val localKeyStore: IosLocalEncryptorKeyStore
) : Encryptor {

    override suspend fun encrypt(value: Long): String {
        try {
            val iv = secureRandomBytes(IV_SIZE_BYTES)
            val encrypted = crypt(kCCEncrypt, value.toBigEndianBytes(), localKeyStore.getSecretKey(), iv)
            return (iv + encrypted).toHex()
        } catch (error: Exception) {
            val message = "Encryption error"
            logE(message, error)
            throw IllegalStateException(message, error)
        }
    }

    override suspend fun decrypt(encryptedValue: String): Long {
        try {
            val combined = encryptedValue.hexToBytes()
            val iv = combined.copyOfRange(0, IV_SIZE_BYTES)
            val cipherText = combined.copyOfRange(IV_SIZE_BYTES, combined.size)
            return crypt(kCCDecrypt, cipherText, localKeyStore.getSecretKey(), iv).toLongBigEndian()
        } catch (error: Exception) {
            val message = "Decryption error"
            logE(message, error)
            throw IllegalStateException(message, error)
        }
    }

    private fun crypt(operation: CCOperation, data: ByteArray, key: ByteArray, iv: ByteArray): ByteArray {
        val outputSize = data.size + kCCBlockSizeAES128.toInt()
        val output = ByteArray(outputSize)
        return memScoped {
            val bytesMoved = alloc<size_tVar>()
            val status = data.usePinned { dataPin ->
                key.usePinned { keyPin ->
                    iv.usePinned { ivPin ->
                        output.usePinned { outputPin ->
                            CCCrypt(
                                operation,
                                kCCAlgorithmAES,
                                kCCOptionPKCS7Padding,
                                keyPin.addressOf(0), key.size.convert(),
                                ivPin.addressOf(0),
                                dataPin.addressOf(0), data.size.convert(),
                                outputPin.addressOf(0), outputSize.convert(),
                                bytesMoved.ptr
                            )
                        }
                    }
                }
            }
            check(status == kCCSuccess) { "CCCrypt failed with status $status" }
            output.copyOf(bytesMoved.value.toInt())
        }
    }

    companion object {
        private const val IV_SIZE_BYTES = 16
    }
}

private fun Long.toBigEndianBytes(): ByteArray =
    ByteArray(Long.SIZE_BYTES) { index ->
        (this ushr (8 * (Long.SIZE_BYTES - 1 - index)) and 0xFF).toByte()
    }

private fun ByteArray.toLongBigEndian(): Long =
    fold(0L) { accumulator, byte -> (accumulator shl 8) or (byte.toLong() and 0xFF) }
