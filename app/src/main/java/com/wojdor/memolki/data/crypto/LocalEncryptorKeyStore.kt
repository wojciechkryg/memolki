package com.wojdor.memolki.data.crypto

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class LocalEncryptorKeyStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private var cachedKey: SecretKey? = null
    private val mutex = Mutex()

    suspend fun initialize() {
        getSecretKey()
    }

    suspend fun getSecretKey(): SecretKey = cachedKey ?: mutex.withLock {
        cachedKey ?: (getExistingKey() ?: generateAndStoreKey()).also { cachedKey = it }
    }

    private suspend fun getExistingKey(): SecretKey? {
        val encoded = dataStore.data.first()[KEY_PREF] ?: return null
        val decoded = Base64.decode(encoded, Base64.DEFAULT)
        return SecretKeySpec(decoded, ALGORITHM)
    }

    private suspend fun generateAndStoreKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
        keyGenerator.init(KEY_SIZE)
        val key = keyGenerator.generateKey()
        val encoded = Base64.encodeToString(key.encoded, Base64.DEFAULT)
        dataStore.edit { it[KEY_PREF] = encoded }
        return key
    }

    companion object {
        private val KEY_PREF = stringPreferencesKey("encryption_key")
        private const val ALGORITHM = "AES"
        private const val KEY_SIZE = 256
    }
}
