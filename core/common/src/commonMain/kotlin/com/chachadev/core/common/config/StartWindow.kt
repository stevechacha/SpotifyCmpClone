package com.chachadev.core.common.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StartWindow(val width: Int, val height: Int) {
    @SerialName("iphone-xr")
    IphoneXR(414, 896),

    @SerialName("iphone-12-pro")
    Iphone12Pro(390, 844),

    @SerialName("galaxy-s20")
    GalaxyS20(412, 915),

    // Desktops
    @SerialName("1366x768")
    Desktop1366x768(1366, 768),

    @SerialName("1024x768")
    Desktop1024x768(1366, 768),

    @SerialName("2k")
    Desktop1920x1080(1920, 1080),

    @SerialName("4k")
    Desktop3840x2160(3840, 2160),
}