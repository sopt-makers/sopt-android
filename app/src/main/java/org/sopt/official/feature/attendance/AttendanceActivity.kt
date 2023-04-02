package org.sopt.official.feature.attendance

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.official.databinding.ActivityAttendanceBinding
import org.sopt.official.domain.entity.attendance.SoptEvent
import org.sopt.official.feature.attendance.model.AttendanceState

@AndroidEntryPoint
class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceBinding
    private val attendanceViewModel by viewModels<AttendanceViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        fetchData()
        observeData()
    }

    private fun initView() {
        initToolbar()
        initRecyclerView()
    }

    private fun fetchData() {
        attendanceViewModel.fetchSoptEvent()
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                attendanceViewModel.soptEvent.collect {
                    when (it) {
                        is AttendanceState.Success -> {
                            updateSoptEventComponent(it.data)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun initToolbar() {
        binding.toolbar.run {
            setSupportActionBar(this)
            setNavigationOnClickListener { this@AttendanceActivity.finish() }
        }
    }

    private fun initRecyclerView() {

    }

    @SuppressLint("SetTextI18n")
    private fun updateSoptEventComponent(soptEvent: SoptEvent) {
        binding.run {
            textInfoEventDate.text = soptEvent.date
            textInfoEventPoint.isVisible = !soptEvent.isAttendancePointAwardedEvent

            textInfoEventName.text = "오늘은 ${soptEvent.eventName} 날이에요"
            (textInfoEventName.text as Spannable).run {
                setSpan(StyleSpan(Typeface.BOLD), 4, 4 + soptEvent.eventName.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            }

            layoutInfoEventLocation.isVisible = (soptEvent.location != null)
            textInfoEventLocation.text = soptEvent.location
        }
    }
}