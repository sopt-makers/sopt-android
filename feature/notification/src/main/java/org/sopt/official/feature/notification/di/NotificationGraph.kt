package org.sopt.official.feature.notification.di

import dev.zacsweers.metro.ContributesTo
import org.sopt.official.common.di.AppScope
import org.sopt.official.feature.notification.all.NotificationActivity
import org.sopt.official.feature.notification.detail.NotificationDetailActivity

@ContributesTo(AppScope::class)
interface NotificationGraph {
    fun notificationActivity(): NotificationActivity
    fun notificationDetailActivity(): NotificationDetailActivity
}
