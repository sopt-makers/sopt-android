package org.sopt.official

import androidx.glance.appwidget.ExperimentalGlanceRemoteViewsApi
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.google.android.glance.tools.viewer.GlanceSnapshot
import com.google.android.glance.tools.viewer.GlanceViewerActivity
import org.sopt.official.widget.AttendanceWidget
import org.sopt.official.widget.AttendanceWidgetReceiver

@OptIn(ExperimentalGlanceRemoteViewsApi::class)
class WidgetViewerActivity : GlanceViewerActivity() {
    override suspend fun getGlanceSnapshot(
        receiver: Class<out GlanceAppWidgetReceiver>
    ) = when (receiver) {
        AttendanceWidgetReceiver::class.java -> GlanceSnapshot(instance = AttendanceWidget())
        else -> throw IllegalArgumentException()
    }

    override fun getProviders() = listOf(AttendanceWidgetReceiver::class.java)
}