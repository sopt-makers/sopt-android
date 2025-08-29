package org.sopt.official.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.sopt.official.plugin.base.BasePlugin

class DeeplinkPlugin : BasePlugin() {
    override fun apply(target: Project) = with(target) {
        applyPlugin("com.google.devtools.ksp")

        dependencies {
            "implementation"(libs.findLibrary("deeplink.dispatch").get())
            "ksp"(libs.findLibrary("deeplink.dispatch.processor").get())
        }
    }
}
