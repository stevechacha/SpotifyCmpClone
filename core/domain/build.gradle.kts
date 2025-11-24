plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {

    sourceSets {

        androidMain.dependencies {

        }

        commonMain.dependencies {
            implementation(libs.kotlinx.coroutinesCore)

        }
        iosMain.dependencies {

        }

        webCommonMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(libs.jetbrains.kotlinx.browser)
        }

    }
}
