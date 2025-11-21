plugins {
    alias(libs.plugins.convention.cmp.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {

    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
        }

        commonMain.dependencies {
            implementation(libs.bundles.ktor.common)

        }
        mobileMain.dependencies {

        }

        webCommonMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(libs.jetbrains.kotlinx.browser)
        }


    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

}