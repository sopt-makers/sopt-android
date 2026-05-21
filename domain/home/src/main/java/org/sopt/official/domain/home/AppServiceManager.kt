package org.sopt.official.domain.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.sopt.official.domain.home.model.AppService
import org.sopt.official.domain.home.usecase.GetAppServiceUseCase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AppServiceManager: app-service 관리 클래스
 * */
@Singleton
class AppServiceManager @Inject constructor(
    private val getAppServiceUseCase: GetAppServiceUseCase
) {
    private val _appServices = MutableStateFlow<List<AppService>?>(null)
    val appServices: StateFlow<List<AppService>?> = _appServices.asStateFlow()

    suspend fun fetchAppServices(forceUpdate: Boolean = false) {
        if (!forceUpdate && _appServices.value != null) return
        
        getAppServiceUseCase()
            .onSuccess { _appServices.value = it }
    }
}