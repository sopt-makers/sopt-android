package org.sopt.official.plugin

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.sopt.official.plugin.base.BasePlugin
import org.sopt.official.plugin.configuration.DependencyManager.addComposeDependencies

class AndroidComposePlugin : BasePlugin() {
    override fun apply(target: Project): Unit = with(target) {
        applyPlugin("org.jetbrains.kotlin.plugin.compose")

        extensions.getByType<CommonExtension>().apply {
            buildFeatures.apply {
                compose = true
            }
        }

        extensions.configure<ComposeCompilerGradlePluginExtension> {
            includeSourceInformation.set(true)
        }

        addComposeDependencies(libs)
    }
}
