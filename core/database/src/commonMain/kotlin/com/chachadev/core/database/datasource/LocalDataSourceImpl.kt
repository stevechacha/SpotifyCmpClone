package com.chachadev.core.database.datasource

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class LocalDataSourceImpl(
    private val settings: Settings
) : LocalDataSource {

    override suspend fun setValue(key: String, value: String) {
        settings[key] = value
    }

    override suspend fun getValue(key: String): String? {
        return settings.getStringOrNull(key)
    }

    override suspend fun removeValue(key: String) {
        settings.remove(key)
    }

    override suspend fun clear() {
        settings.clear()
    }
}


