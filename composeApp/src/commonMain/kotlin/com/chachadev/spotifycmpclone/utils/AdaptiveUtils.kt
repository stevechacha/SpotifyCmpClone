package com.chachadev.spotifycmpclone.utils

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

@Composable
fun currentDeviceConfiguration(): DeviceConfiguration {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
}

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    val isMobile: Boolean
        get() = this in listOf(MOBILE_PORTRAIT, MOBILE_LANDSCAPE)

    val isWideScreen: Boolean
        get() = this in listOf(TABLET_LANDSCAPE, DESKTOP)

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            return with(windowSizeClass) {
                when {
                    minWidthDp < WIDTH_DP_MEDIUM_LOWER_BOUND &&
                            minHeightDp >= HEIGHT_DP_MEDIUM_LOWER_BOUND -> MOBILE_PORTRAIT
                    minWidthDp >= WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp < HEIGHT_DP_MEDIUM_LOWER_BOUND -> MOBILE_LANDSCAPE
                    minWidthDp in WIDTH_DP_MEDIUM_LOWER_BOUND..WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp >= HEIGHT_DP_EXPANDED_LOWER_BOUND -> TABLET_PORTRAIT
                    minWidthDp >= WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp in HEIGHT_DP_MEDIUM_LOWER_BOUND..HEIGHT_DP_EXPANDED_LOWER_BOUND -> TABLET_LANDSCAPE
                    else -> DESKTOP
                }
            }
        }
    }
}

@Composable
fun createNoSpacingPaneScaffoldDirective(): PaneScaffoldDirective {
    val configuration = currentDeviceConfiguration()
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    val maxHorizontalPartitions = when(configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT,
        DeviceConfiguration.MOBILE_LANDSCAPE,
        DeviceConfiguration.TABLET_PORTRAIT -> 1
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> 2
    }

    val verticalPartitionSpacerSize: Dp
    val maxVerticalPartitions: Int

    if(windowAdaptiveInfo.windowPosture.isTabletop) {
        maxVerticalPartitions = 2
        verticalPartitionSpacerSize = 24.dp
    } else {
        maxVerticalPartitions = 1
        verticalPartitionSpacerSize = 0.dp
    }

    return PaneScaffoldDirective(
        maxHorizontalPartitions = maxHorizontalPartitions,
        horizontalPartitionSpacerSize = 0.dp,
        maxVerticalPartitions = maxVerticalPartitions,
        verticalPartitionSpacerSize = verticalPartitionSpacerSize,
        defaultPanePreferredWidth = 360.dp,
        excludedBounds = emptyList()
    )
}