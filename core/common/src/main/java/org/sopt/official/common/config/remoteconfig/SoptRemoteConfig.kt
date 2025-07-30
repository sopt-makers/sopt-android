package org.sopt.official.common.config.remoteconfig

interface SoptRemoteConfig {
    suspend fun getVersionConfig(): Result<UpdateConfigModel>
}
