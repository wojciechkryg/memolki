package com.wojdor.memolki.data.local.datastore.billing

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

open class BillingLocalDataSource(
    private val dataWrite: DataStore<Preferences>
) {
    private val dataRead: Flow<Preferences> = dataWrite.data.catch { _ ->
        emit(emptyPreferences())
    }

    open suspend fun isRewardGranted(purchaseId: String): Boolean =
        dataRead.first()[Key.GRANTED_PURCHASE_IDS]?.contains(purchaseId) == true

    open suspend fun markRewardGranted(purchaseId: String) {
        dataWrite.edit {
            it[Key.GRANTED_PURCHASE_IDS] = (it[Key.GRANTED_PURCHASE_IDS] ?: emptySet()) + purchaseId
        }
    }

    private object Key {
        val GRANTED_PURCHASE_IDS = stringSetPreferencesKey("granted_purchase_ids")
    }
}
