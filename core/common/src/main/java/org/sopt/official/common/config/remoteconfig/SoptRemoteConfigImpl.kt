package org.sopt.official.common.config.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json

class SoptRemoteConfigImpl @Inject constructor(
    private val json: Json,
    private val remoteConfig: FirebaseRemoteConfig
) : SoptRemoteConfig {
    override suspend fun getVersionConfig(): Result<UpdateConfigModel> = runCatching {
        remoteConfig.fetchAndActivate().await()
        val versionConfig = remoteConfig.getString(UPDATE_CONFIG)
        json.decodeFromString<UpdateConfigModel>(versionConfig)
    }
}
