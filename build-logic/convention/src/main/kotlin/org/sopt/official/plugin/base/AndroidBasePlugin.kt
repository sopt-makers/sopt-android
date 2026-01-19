package org.sopt.official.plugin.base

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

abstract class AndroidBasePlugin : BasePlugin() {

    protected fun Project.configureAndroidBase() {
        extensions.getByType<CommonExtension>().apply {
            compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()

            defaultConfig.apply {
                minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
            }

            compileOptions.apply {
                isCoreLibraryDesugaringEnabled = true
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            packaging.resources.excludes.apply {
                add("/META-INF/AL2.0")
                add("/META-INF/LGPL2.1")
                add("kotlin/reflect/*")
            }

            buildFeatures.apply {
                viewBinding = true
                buildConfig = true
            }
        }

        // targetSdk is only available for Application modules
        extensions.findByType(ApplicationExtension::class.java)?.apply {
            defaultConfig.apply {
                targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
            }
        }
    }
}