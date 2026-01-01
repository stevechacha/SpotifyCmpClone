package com.chachadev.core.database.datasource

interface LocalDataSource {
    suspend fun setValue(key: String, value: String)
    suspend fun getValue(key: String): String?
    suspend fun removeValue(key: String)
    suspend fun clear()
}


