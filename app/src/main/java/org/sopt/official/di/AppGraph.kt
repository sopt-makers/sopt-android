package org.sopt.official.di

import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.MetroAppComponentProviders
import dev.zacsweers.metro.ViewModelGraph
import org.sopt.official.common.di.AppScope
import org.sopt.official.network.persistence.SoptDataStore

@DependencyGraph(AppScope::class)
interface AppGraph : MetroAppComponentProviders, ViewModelGraph {
    val dataStore: SoptDataStore
}