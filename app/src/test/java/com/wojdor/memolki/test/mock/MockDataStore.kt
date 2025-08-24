package com.wojdor.memolki.test.mock

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.atomic.AtomicReference

class MockDataStore : DataStore<Preferences> {

    private val preferences = AtomicReference(emptyPreferences())
    private val flow = MutableStateFlow(preferences.get())

    override val data: Flow<Preferences>
        get() = flow

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val newPreferences = transform(preferences.get())
        preferences.set(newPreferences)
        flow.value = newPreferences
        return newPreferences
    }
}
