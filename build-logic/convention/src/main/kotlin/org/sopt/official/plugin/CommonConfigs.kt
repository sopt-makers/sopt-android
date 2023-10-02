package org.sopt.official.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import java.util.Properties

internal fun Project.configureAndroidCommonPlugin() {
    val properties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    apply<AndroidKotlinPlugin>()
    apply<KotlinSerializationPlugin>()
    with(plugins) {
        apply("kotlin-kapt")
        apply("kotlin-parcelize")
    }
    apply<AndroidHiltPlugin>()

    extensions.getByType<BaseExtension>().apply {
        defaultConfig {
            val apiKey = properties["apiKey"] as? String ?: ""
            val dataStoreKey = properties["dataStoreKey"] as? String ?: ""
            val devUrl = properties["devApi"] as? String ?: ""
            val baseUrl = properties["newApi"] as? String ?: ""
            val devOperationUrl = properties["devOperationApi"] as? String ?: ""
            val operationUrl = properties["operationApi"] as? String ?: ""
            val devAmplitudeKey = properties["devAmplitudeKey"] as? String ?: ""
            val amplitudeKey = properties["amplitudeKey"] as? String ?: ""
            manifestPlaceholders["sentryDsn"] = properties["sentryDsn"] as String
            buildConfigField("String", "SOPTAMP_API_KEY", apiKey)
            buildConfigField("String", "SOPTAMP_DATA_STORE_KEY", dataStoreKey)
            buildConfigField("String", "SOPT_DEV_BASE_URL", devUrl)
            buildConfigField("String", "SOPT_BASE_URL", baseUrl)
            buildConfigField("String", "SOPT_DEV_OPERATION_BASE_URL", devOperationUrl)
            buildConfigField("String", "SOPT_OPERATION_BASE_URL", operationUrl)
            buildConfigField("String", "DEV_AMPLITUDE_KEY", devAmplitudeKey)
            buildConfigField("String", "AMPLITUDE_KEY", amplitudeKey)
        }
        buildFeatures.apply {
            dataBinding.enable = true
            viewBinding = true
            buildConfig = true
        }
    }

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        "implementation"(libs.findLibrary("core.ktx").get())
        "implementation"(libs.findLibrary("appcompat").get())
        "implementation"(libs.findLibrary("lifecycle.viewmodel").get())
        "implementation"(libs.findLibrary("lifecycle.runtime").get())
        "implementation"(libs.findLibrary("material").get())
    }
}
