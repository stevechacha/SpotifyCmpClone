package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.PlaylistItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PlaylistItemDto(
    val collaborative: Boolean?,
    val description: String?,
    @SerialName("external_urls")
    val externalUrls: ExternalUrlsDto?,
    val followers: FollowersDto?,
    val href: String?,
    val id: String?,
    val images: List<ImageDto>?,
    val name: String?,
    val owner: OwnerDto?,
    @SerialName("public")
    val publicAccess: Boolean?,
    @SerialName("snapshot_id")
    val snapshotID: String?,
    val tracks: TracksDto?,
    val type: String?,
    val uri: String?
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
                com.chachadev.spotifycmpclone.domain.model.Owner(
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


