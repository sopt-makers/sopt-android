package org.sopt.official.feature.attendance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.feature.attendance.compose.AttendanceRoute
import org.sopt.official.feature.ui.theme.SOPTTheme

@AndroidEntryPoint
class NewAttendanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SOPTTheme(darkTheme = false) {
                AttendanceRoute()
            }
        }
    }
}
