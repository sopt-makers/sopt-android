package org.sopt.official.di

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ManualViewModelAssistedFactory
import dev.zacsweers.metro.MetroViewModelFactory
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.ViewModelAssistedFactory
import org.sopt.official.common.di.AppScope
import kotlin.reflect.KClass

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class SoptViewModelFactory(
    override val viewModelProviders: Map<KClass<out ViewModel>, Provider<ViewModel>>,
    override val assistedFactoryProviders: Map<KClass<out ViewModel>, Provider<ViewModelAssistedFactory>>,
    override val manualAssistedFactoryProviders: Map<KClass<out ManualViewModelAssistedFactory>, Provider<ManualViewModelAssistedFactory>>,
) : MetroViewModelFactory()
