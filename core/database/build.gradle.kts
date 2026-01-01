plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {

    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
        }

        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.kotlinx.coroutinesCore)
        }

        iosMain.dependencies {
        }

        desktopMain.dependencies {
        }

        wasmJsMain.dependencies {
        }
    }
}


