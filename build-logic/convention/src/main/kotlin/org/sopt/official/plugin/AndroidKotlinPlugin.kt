package org.sopt.official.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.sopt.official.plugin.base.BasePlugin
import org.sopt.official.plugin.configuration.DependencyManager.addKotlinDependencies

class AndroidKotlinPlugin : BasePlugin() {
    override fun apply(target: Project) = with(target) {
        applyPlugin("kotlin-android")

        extensions.getByType<KotlinAndroidProjectExtension>().apply {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        addKotlinDependencies(libs)
    }
}
