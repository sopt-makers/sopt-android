package org.sopt.official.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidKotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(plugins) {
            apply("kotlin-android")
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        extensions.getByType<BaseExtension>().apply {
            setCompileSdkVersion(libs.findVersion("compileSdk").get().requiredVersion.toInt())

            defaultConfig {
                minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
                targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
            }

            compileOptions {
                isCoreLibraryDesugaringEnabled = true
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            packagingOptions {
                resources.excludes.apply {
                    add("/META-INF/AL2.0")
                    add("/META-INF/LGPL2.1")
                    add("kotlin/reflect/*")
                }
            }
        }
        extensions.getByType<KotlinAndroidProjectExtension>().apply {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        dependencies {
            "coreLibraryDesugaring"(libs.findLibrary("desugarLibs").get())
            "implementation"(libs.findLibrary("kotlin").get())
            "implementation"(libs.findLibrary("kotlin.coroutines").get())
            "implementation"(libs.findLibrary("kotlin.datetime").get())
        }
    }
}
