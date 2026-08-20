package com.wojdor.memolki.data.crypto

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecSuccess
import platform.Security.kSecRandomDefault

@OptIn(ExperimentalForeignApi::class)
internal fun secureRandomBytes(size: Int): ByteArray {
    val bytes = ByteArray(size)
    val status = bytes.usePinned {
        SecRandomCopyBytes(kSecRandomDefault, size.convert(), it.addressOf(0))
    }
    check(status == errSecSuccess) { "SecRandomCopyBytes failed with status $status" }
    return bytes
}

internal fun ByteArray.toHex(): String =
    joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }

internal fun String.hexToBytes(): ByteArray =
    ByteArray(length / 2) { index ->
        ((this[index * 2].digitToInt(16) shl 4) or this[index * 2 + 1].digitToInt(16)).toByte()
    }
