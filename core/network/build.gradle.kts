import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.buildkonfig)
    alias(libs.plugins.kotlinxSerialization)
}

//buildkonfig {
//    defaultConfigs {
//        val apiKey = gradleLocalProperties(rootDir, rootProject.providers)
//            .getProperty("API_KEY")
//            ?: throw IllegalStateException(
//                "Missing API_KEY property in local.properties"
//            )
//        buildConfigField(FieldSpec.Type.STRING, "API_KEY", apiKey)
//    }
//}

kotlin {

    sourceSets {

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(libs.bundles.ktor.common)
            implementation(libs.bundles.koin.common)
            implementation(libs.kermit)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.okhttp)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(libs.jetbrains.kotlinx.browser)
        }
    }
}
