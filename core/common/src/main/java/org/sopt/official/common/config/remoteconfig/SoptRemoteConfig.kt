package org.sopt.official.common.config.remoteconfig

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json

private const val UPDATE_CONFIG = "android_update_notice"

class SoptRemoteConfig @Inject constructor() {
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

    suspend fun getVersionConfig(): Result<UpdateConfigModel> = runCatching {
        remoteConfig.fetchAndActivate().await()
        val versionConfig = remoteConfig.getString(UPDATE_CONFIG)
        json.decodeFromString<UpdateConfigModel>(versionConfig)
    }.fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it) }
    )
}
