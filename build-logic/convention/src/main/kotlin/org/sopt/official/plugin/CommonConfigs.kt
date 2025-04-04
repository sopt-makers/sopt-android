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
    apply<RetrofitPlugin>()
    with(plugins) {
        apply("kotlin-parcelize")
    }
    apply<AndroidHiltPlugin>()

    extensions.getByType<BaseExtension>().apply {
        defaultConfig {
            val apiKey = properties["apiKey"] as? String ?: ""
            val dataStoreKey = properties["dataStoreKey"] as? String ?: ""
            val pokeDataStoreKey = properties["pokeDataStoreKey"] as? String ?: ""
            val devUrl = properties["devApi"] as? String ?: ""
            val baseUrl = properties["newApi"] as? String ?: ""
            val devOperationUrl = properties["devOperationApi"] as? String ?: ""
            val operationUrl = properties["operationApi"] as? String ?: ""
            val devAmplitudeKey = properties["devAmplitudeKey"] as? String ?: ""
            val amplitudeKey = properties["amplitudeKey"] as? String ?: ""
            val accessTokenKeyAlias = properties["accessTokenKeyAlias"] as? String ?: ""
            val refreshTokenKeyAlias = properties["refreshTokenKeyAlias"] as? String ?: ""
            val playgroundTokenKeyAlias = properties["playgroundTokenKeyAlias"] as? String ?: ""
            val userStatusKeyAlias = properties["userStatusKeyAlias"] as? String ?: ""
            val pushTokenKeyAlias = properties["pushTokenKeyAlias"] as? String ?: ""
            val mockAuthApi = properties["mockAuthApi"] as? String ?: ""
            val platformKeyAlias = properties["platform"] as? String ?: ""
            buildConfigField("String", "SOPTAMP_API_KEY", apiKey)
            buildConfigField("String", "SOPTAMP_DATA_STORE_KEY", dataStoreKey)
            buildConfigField("String", "POKE_DATA_STORE_KEY", pokeDataStoreKey)
            buildConfigField("String", "SOPT_DEV_BASE_URL", devUrl)
            buildConfigField("String", "SOPT_BASE_URL", baseUrl)
            buildConfigField("String", "SOPT_DEV_OPERATION_BASE_URL", devOperationUrl)
            buildConfigField("String", "SOPT_OPERATION_BASE_URL", operationUrl)
            buildConfigField("String", "DEV_AMPLITUDE_KEY", devAmplitudeKey)
            buildConfigField("String", "AMPLITUDE_KEY", amplitudeKey)
            buildConfigField("String", "ACCESS_TOKEN_KEY_ALIAS", accessTokenKeyAlias)
            buildConfigField("String", "REFRESH_TOKEN_KEY_ALIAS", refreshTokenKeyAlias)
            buildConfigField("String", "PLAYGROUND_TOKEN_KEY_ALIAS", playgroundTokenKeyAlias)
            buildConfigField("String", "USER_STATUS_KEY_ALIAS", userStatusKeyAlias)
            buildConfigField("String", "PUSH_TOKEN_KEY_ALIAS", pushTokenKeyAlias)
            buildConfigField("String", "MOCK_AUTH_API", mockAuthApi)
            buildConfigField("String", "PLATFORM", platformKeyAlias)
        }

        buildTypes {
            val debugPlaygroundApi = (properties["debugPlaygroundApi"] as? String).orEmpty()
            val releasePlaygroundApi = (properties["releasePlaygroundApi"] as? String).orEmpty()

            getByName("debug") {
                buildConfigField("String", "PLAYGROUND_API", debugPlaygroundApi)
            }
            getByName("release") {
                buildConfigField("String", "PLAYGROUND_API", releasePlaygroundApi)
            }
        }

        buildFeatures.apply {
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
        "implementation"(libs.findLibrary("activity").get())
        "implementation"(libs.findLibrary("material").get())
        "implementation"(libs.findLibrary("timber").get())
    }
}
