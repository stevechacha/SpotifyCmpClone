import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import com.chachadev.spotifycmpclone.convention.pathToPackageName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class BuildKonfigConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.codingfeline.buildkonfig")
            }

            extensions.configure<BuildKonfigExtension> {
                packageName = target.pathToPackageName()
                exposeObjectWithName = "BuildKonfig"
                defaultConfigs {
                    val properties = gradleLocalProperties(rootDir, rootProject.providers)
                    
                    val apiKey = properties.getProperty("API_KEY")
                        ?: throw IllegalStateException(
                            "Missing API_KEY property in local.properties"
                        )
                    buildConfigField(FieldSpec.Type.STRING, "API_KEY", apiKey)
                    
                    val clientId = properties.getProperty("CLIENT_ID")
                        ?: throw IllegalStateException(
                            "Missing CLIENT_ID property in local.properties"
                        )
                    buildConfigField(FieldSpec.Type.STRING, "CLIENT_ID", clientId)
                    
                    val clientSecret = properties.getProperty("CLIENT_SECRET")
                        ?: throw IllegalStateException(
                            "Missing CLIENT_SECRET property in local.properties"
                        )
                    buildConfigField(FieldSpec.Type.STRING, "CLIENT_SECRET", clientSecret)
                }
            }
        }
    }
}