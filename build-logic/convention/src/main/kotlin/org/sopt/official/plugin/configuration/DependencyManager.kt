package org.sopt.official.plugin.configuration

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.dependencies

object DependencyManager {

    fun Project.addCoreAndroidDependencies(libs: VersionCatalog) {
        dependencies {
            "implementation"(libs.findLibrary("core.ktx").get())
            "implementation"(libs.findLibrary("appcompat").get())
            "implementation"(libs.findLibrary("lifecycle.viewmodel").get())
            "implementation"(libs.findLibrary("lifecycle.runtime").get())
            "implementation"(libs.findLibrary("activity").get())
            "implementation"(libs.findLibrary("material").get())
            "implementation"(libs.findLibrary("timber").get())
        }
    }

    fun Project.addKotlinDependencies(libs: VersionCatalog) {
        dependencies {
            "coreLibraryDesugaring"(libs.findLibrary("desugarLibs").get())
            "implementation"(libs.findLibrary("kotlin").get())
            "implementation"(libs.findLibrary("kotlin.coroutines").get())
            "implementation"(libs.findLibrary("kotlin.datetime").get())
        }
    }

    fun Project.addComposeDependencies(libs: VersionCatalog) {
        dependencies {
            "implementation"(platform(libs.findLibrary("compose-bom").get()))
            "implementation"(libs.findBundle("compose").get())
            "implementation"(libs.findLibrary("coil-compose").get())
            "implementation"(libs.findLibrary("kotlin-collections-immutable").get())
            "implementation"(libs.findLibrary("compose-navigation").get())
            "implementation"(libs.findLibrary("compose-hilt-navigation").get())
        }
    }

    fun Project.addHiltDependencies(libs: VersionCatalog) {
        dependencies {
            "implementation"(libs.findLibrary("hilt").get())
            "ksp"(libs.findLibrary("hilt.ksp").get())
            "ksp"(libs.findLibrary("kotlin.metadata.jvm").get())
            "testImplementation"(libs.findLibrary("hilt.testing").get())
            "kspTest"(libs.findLibrary("hilt.testing.compiler").get())
        }
    }

    fun Project.addRetrofitDependencies(libs: VersionCatalog) {
        dependencies {
            "implementation"(platform(libs.findLibrary("retrofit-bom").get()))
            "implementation"(libs.findLibrary("retrofit").get())
            "implementation"(libs.findLibrary("retrofit-kotlin-serialization-converter").get())
            "implementation"(libs.findLibrary("retrofit-response-type-keeper").get())
        }
    }

    fun Project.addTestDependencies(libs: VersionCatalog) {
        dependencies {
            "testImplementation"(libs.findLibrary("junit").get())
            "debugImplementation"(libs.findLibrary("truth").get())
            "testImplementation"(libs.findLibrary("robolectric").get())
            "androidTestImplementation"(libs.findBundle("androidx.android.test").get())
        }
    }
}