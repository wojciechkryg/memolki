package com.wojdor.memolki.test.mock

import android.content.SharedPreferences

class MockSharedPreferences : SharedPreferences {

    private val map = mutableMapOf<String, Any?>()

    private val editor = MockEditor()

    private inner class MockEditor : SharedPreferences.Editor {
        private val edits = mutableMapOf<String, Any?>()
        private var clear = false

        override fun putString(key: String?, value: String?): SharedPreferences.Editor = apply {
            key?.let { edits[it] = value }
        }

        override fun putStringSet(key: String?, values: Set<String>?): SharedPreferences.Editor = apply {
            key?.let { edits[it] = values }
        }

        override fun putInt(key: String?, value: Int): SharedPreferences.Editor = apply {
            key?.let { edits[it] = value }
        }

        override fun putLong(key: String?, value: Long): SharedPreferences.Editor = apply {
            key?.let { edits[it] = value }
        }

        override fun putFloat(key: String?, value: Float): SharedPreferences.Editor = apply {
            key?.let { edits[it] = value }
        }

        override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor = apply {
            key?.let { edits[it] = value }
        }

        override fun remove(key: String?): SharedPreferences.Editor = apply {
            key?.let { edits[it] = null }
        }

        override fun clear(): SharedPreferences.Editor = apply {
            clear = true
        }

        override fun commit(): Boolean {
            apply()
            return true
        }

        override fun apply() {
            if (clear) {
                this@MockSharedPreferences.map.clear()
            }
            edits.forEach { (key, value) ->
                if (value == null) {
                    this@MockSharedPreferences.map.remove(key)
                } else {
                    this@MockSharedPreferences.map[key] = value
                }
            }
            edits.clear()
        }
    }

    override fun contains(p0: String?): Boolean {
        return map.contains(p0)
    }

    override fun edit(): SharedPreferences.Editor {
        return editor
    }

    override fun getAll(): Map<String, *> {
        return map.toMap()
    }

    override fun getBoolean(p0: String?, p1: Boolean): Boolean {
        return map[p0] as? Boolean ?: p1
    }

    override fun getFloat(p0: String?, p1: Float): Float {
        return map[p0] as? Float ?: p1
    }

    override fun getInt(p0: String?, p1: Int): Int {
        return map[p0] as? Int ?: p1
    }

    override fun getLong(p0: String?, p1: Long): Long {
        return map[p0] as? Long ?: p1
    }

    override fun getString(p0: String?, p1: String?): String? {
        return map[p0] as? String ?: p1
    }

    @Suppress("UNCHECKED_CAST")
    override fun getStringSet(
        p0: String?,
        p1: Set<String?>?
    ): Set<String?>? {
        return map[p0] as? Set<String?> ?: p1
    }

    override fun registerOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        // No-op
    }

    override fun unregisterOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        // No-op
    }
}
