plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {

    sourceSets {

        androidMain.dependencies {

        }

        commonMain.dependencies {

        }
        iosMain.dependencies {

        }

        webCommonMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(libs.jetbrains.kotlinx.browser)
        }

    }
}
