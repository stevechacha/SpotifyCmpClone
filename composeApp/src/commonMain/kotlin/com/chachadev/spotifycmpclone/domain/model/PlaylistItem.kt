package com.chachadev.spotifycmpclone.domain.model

data class PlaylistItem(
    val collaborative: Boolean?,
    val description: String?,
    val externalUrls: ExternalUrls?,
    val followers: Followers?,
    val href: String?,
    val id: String?,
    val images: List<Image>?,
    val name: String?,
    val owner: Owner?,
    val publicAccess: Boolean?,
    val snapshotID: String?,
    val tracks: Tracks?,
    val type: String?,
    val uri: String?
)