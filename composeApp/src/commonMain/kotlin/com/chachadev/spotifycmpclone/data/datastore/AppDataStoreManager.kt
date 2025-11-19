package com.chachadev.spotifycmpclone.data.datastore

import com.chachadev.spotifycmpclone.utils.Context
import com.chachadev.spotifycmpclone.utils.getData
import com.chachadev.spotifycmpclone.utils.putData


class AppDataStoreManager(val context: Context?) : AppDataStore {

    override suspend fun setValue(
        key: String,
        value: String
    ) {
        context.putData(key, value)
    }

    override suspend fun readValue(
        key: String,
    ): String? {
        return context.getData(key)
    }
}