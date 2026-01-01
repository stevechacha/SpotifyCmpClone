package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.Playlist
import com.chachadev.core.domain.model.PlaylistOwner
import com.chachadev.core.domain.model.PlaylistTracks
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDto(
    val id: String,
    val name: String,
    val description: String? = null,
    val images: List<ImageDto>,
    val owner: OwnerDto? = null,
    val tracks: TracksInfoDto? = null,
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null
) {
    fun toDomain(): Playlist {
        return Playlist(
            id = id,
            name = name,
            description = description,
            images = images.map { it.toDomain() },
            owner = owner?.toDomain(),
            tracks = tracks?.toDomain(),
            externalUrls = externalUrls?.toDomain()
        )
    }
}



@Serializable
data class TracksInfoDto(
    val total: Int
) {
    fun toDomain(): PlaylistTracks {
        return PlaylistTracks(total = total)
    }
}

