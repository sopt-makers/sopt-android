package org.sopt.official.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(plugins) {
            apply("org.jetbrains.kotlin.plugin.compose")
        }
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        extensions.getByType<BaseExtension>().apply {
            buildFeatures.apply {
                compose = true
            }
        }
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            includeSourceInformation.set(true)
        }
        dependencies {
            "implementation"(platform(libs.findLibrary("compose-bom").get()))
            "implementation"(libs.findBundle("compose").get())
            "implementation"(libs.findLibrary("coil-compose").get())
            "implementation"(libs.findLibrary("kotlin-collections-immutable").get())
        }
    }
}
