package org.sopt.official.config.remoteconfig

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.serialization.json.Json
import timber.log.Timber

private const val UPDATE_CONFIG = "android_update_notice"

class SoptRemoteConfig {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(mapOf(UPDATE_CONFIG to ""))
        }
    }

    fun addOnVersionFetchCompleteListener(callback: (UpdateConfigResponseDto) -> Unit) {
        try {
            val fetchTask = remoteConfig.fetchAndActivate()
            fetchTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val versionConfig = remoteConfig.getString(UPDATE_CONFIG)
                    Timber.d(versionConfig)
                    callback(json.decodeFromString<UpdateConfigResponseDto>(versionConfig))
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
