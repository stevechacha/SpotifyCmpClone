package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.PlaylistItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PlaylistItemDto(
    val collaborative: Boolean? = null,
    val description: String? = null,
    @SerialName("external_urls")
    val externalUrls: ExternalUrlsDto? = null,
    val followers: FollowersDto? = null,
    val href: String? = null,
    val id: String? = null,
    val images: List<ImageDto>? = null,
    val name: String? = null,
    val owner: OwnerDto? = null,
    @SerialName("public")
    val publicAccess: Boolean? = null,
    @SerialName("snapshot_id")
    val snapshotID: String? = null,
    val tracks: TracksDto? = null,
    val type: String? = null,
    val uri: String? = null
) {
    fun toDomain(): PlaylistItem {
        return PlaylistItem(
            collaborative = collaborative,
            description = description,
            externalUrls = externalUrls?.toDomain(),
            followers = followers?.toDomain(),
            href = href,
            id = id,
            images = images?.map { it.toDomain() },
            name = name,
            owner = owner?.let { 
                com.chachadev.core.domain.model.Owner(
                    externalUrls = null,
                    followers = null,
                    href = null,
                    id = it.id,
                    type = null,
                    uri = null,
                    displayName = it.displayName
                )
            },
            publicAccess = publicAccess,
            snapshotID = snapshotID,
            tracks = tracks?.toDomain(),
            type = type,
            uri = uri
        )
    }
}


