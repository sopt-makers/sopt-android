package org.sopt.official.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import timber.log.Timber

class AttendanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = AttendanceWidget()

    companion object {
        suspend fun updateWidget(score: Double, context: Context) {
            val glanceId = GlanceAppWidgetManager(context).getGlanceIds(AttendanceWidget::class.java).last()
            updateAppWidgetState(context, glanceId) { prefs ->
                prefs[AttendanceWidget.ATTENDANCE_SCORE_KEY] = score
            }
            AttendanceWidget().updateAll(context)
        }
    }
}
