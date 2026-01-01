package com.chachadev.core.domain.model

/**
 * Represents a track that was recently played, including context information
 * about where it was played from (playlist, album, artist, etc.)
 */
data class RecentlyPlayedItem(
    val track: com.chachadev.core.domain.model.Track,
    val playedAt: String, // ISO 8601 timestamp
    val context: com.chachadev.core.domain.model.PlayContext?
)

/**
 * Represents the context where a track was played from
 */
data class PlayContext(
    val type: String, // "playlist", "album", "artist", "show", etc.
    val href: String?,
    val uri: String?,
    val externalUrls: com.chachadev.core.domain.model.ExternalUrls?
) {
    /**
     * Extracts the ID from the Spotify URI.
     * URI format: "spotify:playlist:37i9dQZF1DXcBWIGoYBM5M"
     * Returns the ID part (e.g., "37i9dQZF1DXcBWIGoYBM5M")
     * CRITICAL: Only returns ID if URI type EXACTLY matches context type to prevent wrong navigation.
     */
    fun extractId(): String? {
        if (uri == null) {
            println("PlayContext: URI is null, cannot extract ID")
            return null
        }
        
        val parts = uri.split(":")
        println("PlayContext: Extracting ID from URI=$uri, type=$type, parts=$parts")
        
        // URI format: "spotify:playlist:ID" or "spotify:album:ID" or "spotify:artist:ID"
        if (parts.size < 3) {
            println("PlayContext: Invalid URI format, expected at least 3 parts separated by ':'")
            return null
        }
        
        val uriType = parts.getOrNull(1) // e.g., "playlist", "album", "artist"
        val extractedId = parts.getOrNull(2) // The actual ID
        
        if (extractedId.isNullOrBlank()) {
            println("PlayContext: Extracted ID is null or blank")
            return null
        }
        
        // CRITICAL: Only return ID if URI type EXACTLY matches context type
        if (uriType != null && type != null && uriType == type) {
            println("PlayContext: ✅ Valid ID extracted: uriType=$uriType, contextType=$type, id=$extractedId")
            return extractedId
        } else {
            println("PlayContext: ❌ REJECTED - URI type ($uriType) does not match context type ($type), returning null to prevent wrong navigation")
            return null
        }
    }
}

