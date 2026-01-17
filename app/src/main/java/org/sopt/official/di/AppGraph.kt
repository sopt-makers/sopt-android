package org.sopt.official.di

import android.app.Application
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import org.sopt.official.common.di.AppScope
import org.sopt.official.common.navigator.NavigatorGraph
import org.sopt.official.config.messaging.SoptFirebaseMessagingService
import org.sopt.official.feature.auth.utils.di.GoogleGraph
import org.sopt.official.feature.mypage.di.AuthGraph
import org.sopt.official.feature.poke.di.PokeGraph
import org.sopt.official.network.persistence.SoptDataStore
import org.sopt.official.webview.di.WebViewGraph

@DependencyGraph(AppScope::class)
interface AppGraph : MetroAppComponentProviders, ViewModelGraph, NavigatorGraph, AuthGraph, WebViewGraph, PokeGraph, GoogleGraph {
    override val dataStore: SoptDataStore
    fun inject(service: SoptFirebaseMessagingService)

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
