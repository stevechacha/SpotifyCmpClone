package com.chachadev.core.domain.model

data class PlaylistItem(
    val collaborative: Boolean?,
    val description: String?,
    val externalUrls: com.chachadev.core.domain.model.ExternalUrls?,
    val followers: com.chachadev.core.domain.model.Followers?,
    val href: String?,
    val id: String?,
    val images: List<com.chachadev.core.domain.model.Image>?,
    val name: String?,
    val owner: com.chachadev.core.domain.model.Owner?,
    val publicAccess: Boolean?,
    val snapshotID: String?,
    val tracks: com.chachadev.core.domain.model.Tracks?,
    val type: String?,
    val uri: String?
)