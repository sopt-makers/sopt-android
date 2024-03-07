package org.sopt.official.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        extensions.getByType<BaseExtension>().apply {
            buildFeatures.apply {
                compose = true
            }
            composeOptions {
                kotlinCompilerExtensionVersion = libs.findVersion("compose-compiler").get().requiredVersion
            }
        }
    }
}
