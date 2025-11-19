package com.chachadev.spotifycmpclone.utils

import okio.Path.Companion.toPath

/*fun createDataStore(producePath: () -> String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath {
        producePath().toPath()
    }
}*/

internal const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"