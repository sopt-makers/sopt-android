package org.sopt.official.plugin.configuration

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.util.Properties


object ConfigurationManager {

    fun loadProperties(project: Project): Properties {
        return Properties().apply {
            val localPropertiesFile = project.rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                load(localPropertiesFile.inputStream())
            }
        }
    }

    fun CommonExtension.configureBuildConfigFields(properties: Properties) {
        defaultConfig.apply {
            // API Keys and URLs
            buildConfigField("String", "SOPTAMP_API_KEY", properties.getQuotedProperty("apiKey"))
            buildConfigField("String", "SOPTAMP_DATA_STORE_KEY", properties.getQuotedProperty("dataStoreKey"))
            buildConfigField("String", "POKE_DATA_STORE_KEY", properties.getQuotedProperty("pokeDataStoreKey"))

            // Base URLs
            buildConfigField("String", "SOPT_DEV_BASE_URL", properties.getQuotedProperty("devApi"))
            buildConfigField("String", "SOPT_BASE_URL", properties.getQuotedProperty("newApi"))
            buildConfigField("String", "SOPT_DEV_OPERATION_BASE_URL", properties.getQuotedProperty("devOperationApi"))
            buildConfigField("String", "SOPT_OPERATION_BASE_URL", properties.getQuotedProperty("operationApi"))

            // Analytics
            buildConfigField("String", "DEV_AMPLITUDE_KEY", properties.getQuotedProperty("devAmplitudeKey"))
            buildConfigField("String", "AMPLITUDE_KEY", properties.getQuotedProperty("amplitudeKey"))

            // Security Keys
            buildConfigField("String", "ACCESS_TOKEN_KEY_ALIAS", properties.getQuotedProperty("accessTokenKeyAlias"))
            buildConfigField("String", "REFRESH_TOKEN_KEY_ALIAS", properties.getQuotedProperty("refreshTokenKeyAlias"))
            buildConfigField("String", "PLAYGROUND_TOKEN_KEY_ALIAS", properties.getQuotedProperty("playgroundTokenKeyAlias"))
            buildConfigField("String", "USER_STATUS_KEY_ALIAS", properties.getQuotedProperty("userStatusKeyAlias"))
            buildConfigField("String", "PUSH_TOKEN_KEY_ALIAS", properties.getQuotedProperty("pushTokenKeyAlias"))

            // Auth
            buildConfigField("String", "DEV_AUTH_API", properties.getQuotedProperty("devAuthApi"))
            buildConfigField("String", "PROD_AUTH_API", properties.getQuotedProperty("prodAuthApi"))
            buildConfigField("String", "PLATFORM", properties.getQuotedProperty("platform"))
            buildConfigField("String", "DEV_SERVER_CLIENT_ID", properties.getQuotedProperty("devServerClientId"))
            buildConfigField("String", "PROD_SERVER_CLIENT_ID", properties.getQuotedProperty("prodServerClientId"))
        }

        buildTypes.apply {
            getByName("debug").apply {
                buildConfigField(
                    "String", "PLAYGROUND_API",
                    properties.getQuotedProperty("debugPlaygroundApi")
                )

                manifestPlaceholders["PLAYGROUND_HOST"] = properties.getProperty("dev_playground_host")
            }
            getByName("release").apply {
                buildConfigField(
                    "String", "PLAYGROUND_API",
                    properties.getQuotedProperty("releasePlaygroundApi")
                )

                manifestPlaceholders["PLAYGROUND_HOST"] = properties.getProperty("prod_playground_host")
            }
        }
    }

    private fun Properties.getQuotedProperty(key: String): String {
        val value = getProperty(key) ?: ""
        // If the value is already quoted, don't add additional quotes
        return if (value.startsWith("\"") && value.endsWith("\"")) {
            value
        } else {
            "\"$value\""
        }
    }
}