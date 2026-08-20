package com.wojdor.memolki.data.crypto

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class IosLocalEncryptorKeyStore(
    private val dataStore: DataStore<Preferences>
) : LocalEncryptorKeyStore {

    private var cachedKey: ByteArray? = null
    private val mutex = Mutex()

    override suspend fun initialize() {
        getSecretKey()
    }

    suspend fun getSecretKey(): ByteArray = cachedKey ?: mutex.withLock {
        cachedKey ?: (getExistingKey() ?: generateAndStoreKey()).also { cachedKey = it }
    }

    private suspend fun getExistingKey(): ByteArray? =
        dataStore.data.first()[KEY_PREF]?.hexToBytes()

    private suspend fun generateAndStoreKey(): ByteArray {
        val key = secureRandomBytes(KEY_SIZE_BYTES)
        dataStore.edit { it[KEY_PREF] = key.toHex() }
        return key
    }

    companion object {
        private val KEY_PREF = stringPreferencesKey("encryption_key")
        private const val KEY_SIZE_BYTES = 32
    }
}
