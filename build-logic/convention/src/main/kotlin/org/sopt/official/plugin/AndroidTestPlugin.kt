package org.sopt.official.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidTestPlugin: Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        apply<JUnit5Plugin>()
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        extensions.getByType<BaseExtension>().apply {
            testOptions {
                unitTests {
                    isIncludeAndroidResources = true
                }
            }
        }

        dependencies {
            "testImplementation"(libs.findLibrary("junit").get())
            "testImplementation"(libs.findLibrary("truth").get())
            "testImplementation"(libs.findLibrary("robolectric").get())
        }
    }
}
