import com.chachadev.spotifycmpclone.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension


class CmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.chachadev.convention.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            val compose = extensions.getByType<ComposeExtension>().dependencies
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.material)
                        implementation(compose.material3)
                        implementation(compose.animation)
                        implementation(compose.animationGraphics)
                        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                        api(compose.materialIconsExtended)
                        implementation(compose.ui)
                        implementation(compose.components.resources)
                        implementation(compose.components.uiToolingPreview)
                    }
                }
            }
        }
    }
}