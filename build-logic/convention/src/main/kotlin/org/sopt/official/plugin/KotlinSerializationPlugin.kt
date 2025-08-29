package org.sopt.official.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.sopt.official.plugin.base.BasePlugin

class KotlinSerializationPlugin : BasePlugin() {
    override fun apply(target: Project) = with(target) {
        applyPlugin("org.jetbrains.kotlin.plugin.serialization")

        dependencies {
            "implementation"(libs.findLibrary("kotlin.serialization.json").get())
        }
    }
}
