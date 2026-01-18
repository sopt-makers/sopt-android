package org.sopt.official.feature.schedule.di

import dev.zacsweers.metro.ContributesTo
import org.sopt.official.common.di.AppScope
import org.sopt.official.feature.schedule.ScheduleActivity

@ContributesTo(AppScope::class)
interface ScheduleGraph {
    fun scheduleActivity(): ScheduleActivity
}
