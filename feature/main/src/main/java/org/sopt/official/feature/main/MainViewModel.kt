package org.sopt.official.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.home.usecase.GetAppServiceUseCase

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAppServiceUseCase: GetAppServiceUseCase
) : ViewModel() {
    private val _mainTabs = MutableStateFlow(MainTab.getActiveTabs(emptyList()))
    val mainTabs: StateFlow<List<MainTab>>
        get() = _mainTabs.asStateFlow()

    init {
        fetchMainTabs()
    }

    private fun fetchMainTabs() {
        viewModelScope.launch {
            getAppServiceUseCase()
                .onSuccess { appServices ->
                    updateMainTabs(appServices.map { it.deepLink })
                }
        }
    }

    private fun updateMainTabs(tabs: List<String>) {
        _mainTabs.update {
            MainTab.getActiveTabs(tabs)
        }
    }
}
