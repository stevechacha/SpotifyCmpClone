import com.chachadev.spotifycmpclone.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class CmpFeatureConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.chachadev.convention.cmp.library")
            }

            val compose = extensions.getByType<ComposeExtension>().dependencies
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    androidMain.dependencies {
                        implementation(project.dependencies.platform(libs.findLibrary("koin-bom").get()))
                        implementation(libs.findLibrary("androidx-activity-compose").get())
                        implementation(libs.findLibrary("androidx-appcompat").get())
                        implementation(libs.findLibrary("androidx-core-ktx").get())
                        implementation(libs.findLibrary("koin-android").get())
                        implementation(libs.findLibrary("koin-androidx-compose").get())
                        implementation(libs.findLibrary("koin-androidx-navigation").get())
                        implementation(libs.findLibrary("koin-core-viewmodel").get())
                        implementation(libs.findLibrary("ktor-client-okhttp").get())

                    }
                    commonMain.dependencies {
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.material)
                        implementation(compose.material3)
                        implementation(compose.materialIconsExtended)
                        implementation(compose.animationGraphics)
                        implementation(compose.material3AdaptiveNavigationSuite)
                        implementation(project.dependencies.platform(libs.findLibrary("koin-bom").get()))
                        implementation(libs.findLibrary("koin-compose").get())
                       implementation(libs.findLibrary("koin-compose-viewmodel").get())
                        implementation(libs.findLibrary("jetbrains-compose-navigation").get())
                        implementation(libs.findLibrary("jetbrains-compose-backhandler").get())
                        implementation(libs.findLibrary("material3-adaptive").get())
                        implementation(libs.findLibrary("material3-adaptive-layout").get())
                        implementation(libs.findLibrary("material3-adaptive-navigation").get())
                        implementation(libs.findLibrary("ktor-client-core").get())
                        implementation(libs.findLibrary("ktor-client-auth").get())
                        implementation(libs.findLibrary("ktor-client-logging").get())
                        implementation(libs.findLibrary("ktor-client-contentNegotiation").get())
                        implementation(libs.findLibrary("ktor-serialization-kotlinx-json").get())
                        implementation(libs.findLibrary("kotlinx-serializationJson").get())
                        implementation(libs.findLibrary("kotlinx-coroutinesCore").get())
                        implementation(libs.findLibrary("kotlinx-datetime").get())
                        implementation(libs.findLibrary("jetbrains-compose-viewmodel").get())
                        implementation(libs.findLibrary("jetbrains-lifecycle-viewmodel").get())
                       implementation(libs.findLibrary("jetbrains-lifecycle-compose").get())
                       implementation(libs.findLibrary("jetbrains-lifecycle-viewmodel-savedstate").get())
                       implementation(libs.findLibrary("jetbrains-savedstate").get())
                       implementation(libs.findLibrary("jetbrains-bundle").get())

                    }

                    jvmMain.dependencies {
                        implementation(compose.desktop.common)
                        implementation(compose.desktop.currentOs)
                        implementation(libs.findLibrary("ktor-client-okhttp").get())
                        implementation(libs.findLibrary("kotlinx-coroutinesSwing").get())
                    }



                    iosMain.dependencies {
                        implementation(libs.findLibrary("ktor-client-ios").get())
                        implementation(libs.findLibrary("ktor-client-darwin").get())

                    }

                    webMain.dependencies {
                        implementation(libs.findLibrary("ktor-client-js").get())
                        implementation(libs.findLibrary("jetbrains-kotlinx-browser").get())

                    }

                }
            }

            /*dependencies {
                "commonMainImplementation"(project(":core:presentation"))
                "commonMainImplementation"(project(":core:designsystem"))
            }*/
        }
    }
}