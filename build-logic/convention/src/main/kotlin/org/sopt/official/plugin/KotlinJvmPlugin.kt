package org.sopt.official.plugin

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.sopt.official.plugin.base.BasePlugin

class KotlinJvmPlugin : BasePlugin() {
    override fun apply(target: Project): Unit = with(target) {
        applyPlugin("org.jetbrains.kotlin.jvm")
        applyPlugin("java-library")

        tasks.withType(KotlinCompile::class.java).configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }
}
