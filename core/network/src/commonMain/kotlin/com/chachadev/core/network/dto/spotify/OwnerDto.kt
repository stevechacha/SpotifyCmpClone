package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.PlaylistOwner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OwnerDto(
    val id: String,
    @SerialName("display_name") val displayName: String? = null
) {
    fun toDomain(): PlaylistOwner {
        return PlaylistOwner(
            id = id,
            displayName = displayName
        )
    }
}
/*

data class OwnerDto(
    val externalUrls: ExternalUrlsDto?,
    val followers: FollowersDto?,
    val href: String?,
    val id: String?,
    val type: String?,
    val uri: String?,
    val displayName: String?
)
*/
