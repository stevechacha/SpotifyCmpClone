@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package com.chachadev.spotifycmpclone.convention

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

private val hierarchyTemplate = KotlinHierarchyTemplate {
    withSourceSetTree(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test,
    )

    common {
        withCompilations { true }

        group("mobile") {
            withAndroidTarget()
            group("ios") {
                withIos()
            }
        }

        group("jvmCommon") {
            withAndroidTarget()
            withJvm()
        }

        group("webCommon"){
            withJs()
            withWasmJs()
        }

        group("skiko") {
            withJvm()
            withJs()
            withWasmJs()
            withIosX64()
            withIosArm64()
            withIosSimulatorArm64()
            withMacosX64()
            withMacosArm64()
        }

        group("native") {
            withNative()

            group("apple") {
                withApple()

                group("ios") {
                    withIos()
                }

                group("macos") {
                    withMacos()
                }
            }
        }
    }
}

fun KotlinMultiplatformExtension.applyHierarchyTemplate() {
    applyHierarchyTemplate(hierarchyTemplate)
}