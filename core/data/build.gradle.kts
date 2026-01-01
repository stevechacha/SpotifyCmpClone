plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {

    sourceSets {


        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.network)
            implementation(projects.core.database)
            implementation(libs.bundles.ktor.common)
            implementation(libs.bundles.koin.common)
            implementation(libs.kotlinx.coroutinesCore)

        }
    }
}
