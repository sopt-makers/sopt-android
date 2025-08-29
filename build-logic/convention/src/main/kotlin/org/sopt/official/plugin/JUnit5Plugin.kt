package org.sopt.official.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.sopt.official.plugin.base.BasePlugin

class JUnit5Plugin : BasePlugin() {
    override fun apply(target: Project): Unit = with(target) {
        applyPlugin("de.mannodermaus.android-junit5")

        dependencies {
            "testImplementation"(libs.findBundle("junit5.test").get())
            "androidTestImplementation"(libs.findLibrary("junit5").get())
            "androidTestImplementation"(libs.findLibrary("junit5.params").get())
            "androidTestImplementation"(libs.findLibrary("junit5.android.test.core").get())
            "androidTestRuntimeOnly"(libs.findLibrary("junit5.android.test.runner").get())
        }
    }
}
