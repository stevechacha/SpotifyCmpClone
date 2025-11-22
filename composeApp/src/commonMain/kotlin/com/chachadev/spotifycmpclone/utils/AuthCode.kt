package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable

@Composable
expect fun getStoredAuthCode(): String?

@Composable
expect fun clearStoredAuthCode()

expect fun storeAuthCode(code: String)