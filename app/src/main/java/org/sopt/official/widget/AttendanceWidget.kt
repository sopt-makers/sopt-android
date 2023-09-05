package org.sopt.official.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import org.sopt.official.R
import org.sopt.official.util.widget.AndroidResourceImageProvider
import org.sopt.official.util.widget.LocalColorProvider
import java.io.File

class AttendanceWidget : GlanceAppWidget() {
    override val stateDefinition = AttendanceGlanceStateDefinition

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) = provideContent { Content() }

    @Composable
    private fun Content() {
        val state = currentState<Preferences>()
        val attendanceScore = state[ATTENDANCE_SCORE_KEY] ?: DEFAULT_VALUE
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 26.dp, bottom = 16.dp)
                .background(
                    AndroidResourceImageProvider(R.drawable.background_widget),
                    ContentScale.FillBounds
                )
        ) {
            if (!isError(attendanceScore)) {
                Text(
                    text = "당신의 출석 점수는?",
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
                Box(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .background(LocalColorProvider(R.color.purple_100))
                        .cornerRadius(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = attendanceScore.toString(),
                        style = TextStyle(
                            color = LocalColorProvider(R.color.white_100),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
            if (isError(attendanceScore)) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "출석 정보를 불러올 수 없습니다.",
                        style = TextStyle(
                            color = LocalColorProvider(R.color.white_100),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                }
            }
        }
    }

    override val sizeMode: SizeMode
        get() = SizeMode.Single

    private fun isError(score: Double) = score <= DEFAULT_VALUE

    companion object {
        val ATTENDANCE_SCORE_KEY = doublePreferencesKey("attendance_score")
        const val DEFAULT_VALUE = -5.0
        const val INVALID_TOKEN_ERROR = -6.0
        const val NETWORK_ERROR = -7.0
    }

    /*
    * 위젯은 만들어질때마다 새로운 DataStore를 참조하게 되는데
    * 위젯을 중복하여 여러 개 만드는 경우 다른 DataStore를 참조하게 되어
    * 기존에 사용하던 DataStore에 접근할 수 없게 된다.
    * 따라서 StateDefinition을 재정의하여
    * 동일한 DataStore를 참조할 수 있게 만든다.
    * */
    object AttendanceGlanceStateDefinition : GlanceStateDefinition<Preferences> {
        override suspend fun getDataStore(
            context: Context,
            fileKey: String
        ): DataStore<Preferences> {
            return context.dataStore
        }

        override fun getLocation(
            context: Context,
            fileKey: String
        ): File {
            return File(context.applicationContext.filesDir, "datastore/$FILE_NAME")
        }

        private const val FILE_NAME = "attendance_widget_store"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = FILE_NAME)
    }
}
