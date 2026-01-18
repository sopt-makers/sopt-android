package org.sopt.official.feature.fortune.di

import dev.zacsweers.metro.ContributesTo
import org.sopt.official.common.di.AppScope
import org.sopt.official.feature.fortune.FortuneActivity

@ContributesTo(AppScope::class)
interface FortuneGraph {
    fun fortuneActivity(): FortuneActivity
}
