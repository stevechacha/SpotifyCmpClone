package com.chachadev.core.network.auth

interface AuthProvider {
    suspend fun getValidToken(): String
    fun getRedirectUri(): String
    fun getSignInUrl(): String
    suspend fun exchangeCodeForToken(code: String): Result<OAuthTokenResponse>
    fun setSignedIn(signedIn: Boolean)
    val isSignedIn: kotlinx.coroutines.flow.StateFlow<Boolean>
}

@kotlinx.serialization.Serializable
data class OAuthTokenResponse(
    @kotlinx.serialization.SerialName("access_token") val accessToken: String,
    @kotlinx.serialization.SerialName("expires_in") val expiresIn: Long,
    @kotlinx.serialization.SerialName("refresh_token") val refreshToken: String? = null,
    @kotlinx.serialization.SerialName("scope") val scope: String? = null,
    @kotlinx.serialization.SerialName("token_type") val tokenType: String
)


