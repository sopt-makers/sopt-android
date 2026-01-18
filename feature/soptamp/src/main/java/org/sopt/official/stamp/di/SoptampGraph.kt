package org.sopt.official.stamp.di

import dev.zacsweers.metro.ContributesTo
import org.sopt.official.common.di.AppScope
import org.sopt.official.stamp.SoptampActivity

@ContributesTo(AppScope::class)
interface SoptampGraph {
    fun soptampActivity(): SoptampActivity
}
