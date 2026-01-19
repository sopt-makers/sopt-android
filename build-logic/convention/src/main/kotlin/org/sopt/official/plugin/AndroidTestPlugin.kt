package org.sopt.official.plugin

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.sopt.official.plugin.base.BasePlugin
import org.sopt.official.plugin.configuration.DependencyManager.addTestDependencies

class AndroidTestPlugin : BasePlugin() {
    override fun apply(target: Project): Unit = with(target) {
        apply<JUnit5Plugin>()

        extensions.getByType<CommonExtension>().apply {
            defaultConfig.apply {
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testInstrumentationRunnerArguments["runnerBuilder"] =
                    "de.mannodermaus.junit5.AndroidJUnit5Builder"
            }

            testOptions.unitTests.apply {
                isIncludeAndroidResources = true
            }

            packaging.resources.excludes.add("META-INF/LICENSE*")
        }

        addTestDependencies(libs)
    }
}
