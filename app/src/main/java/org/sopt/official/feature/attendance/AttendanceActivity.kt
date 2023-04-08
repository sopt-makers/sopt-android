package org.sopt.official.feature.attendance

import android.annotation.SuppressLint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.official.databinding.ActivityAttendanceBinding
import org.sopt.official.domain.entity.attendance.*
import org.sopt.official.feature.attendance.adapter.AttendanceAdapter
import org.sopt.official.feature.attendance.model.AttendanceState
import org.sopt.official.feature.attendance.util.dpToPx

@AndroidEntryPoint
class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceBinding
    private val attendanceViewModel by viewModels<AttendanceViewModel>()
    private lateinit var attendanceAdapter: AttendanceAdapter

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
        attendanceViewModel.run {
            fetchSoptEvent()
            fetchAttendanceHistory()
        }
    }

    private fun observeData() {
        observeSoptEvent()
        observeAttendanceUserInfo()
        observeAttendanceSummary()
        observeAttendanceLog()
    }

    private fun initToolbar() {
        binding.toolbar.run {
            setSupportActionBar(this)
            setNavigationOnClickListener { this@AttendanceActivity.finish() }
        }
    }

    private fun initRecyclerView() {
        attendanceAdapter = AttendanceAdapter()
        binding.recyclerViewAttendanceHistory.run {
            adapter = attendanceAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    when (parent.getChildAdapterPosition(view)) {
                        0 -> {
                            outRect.set(24.dpToPx(), 32.dpToPx(), 24.dpToPx(), 12.dpToPx())
                        }
                        attendanceAdapter.itemCount - 1 -> {
                            outRect.set(24.dpToPx(), 12.dpToPx(), 24.dpToPx(), 32.dpToPx())
                        }
                        else -> {
                            outRect.set(24.dpToPx(), 12.dpToPx(), 24.dpToPx(), 12.dpToPx())
                        }
                    }
                }
            })
        }
    }

    private fun observeSoptEvent() {
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

    private fun observeAttendanceUserInfo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                attendanceViewModel.attendanceUserInfo.collect {
                    when (it) {
                        is AttendanceState.Success -> {
                            updateAttendanceUserInfo(it.data)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun observeAttendanceSummary() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                attendanceViewModel.attendanceSummary.collect {
                    when (it) {
                        is AttendanceState.Success -> {
                            updateAttendanceSummary(it.data)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun observeAttendanceLog() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                attendanceViewModel.attendanceLog.collect {
                    when (it) {
                        is AttendanceState.Success -> {
                            updateAttendanceLog(it.data)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateSoptEventComponent(soptEvent: SoptEvent) {
        binding.run {
            when (soptEvent.eventType) {
                EventType.NO_SESSION -> {
                    layoutInfoEventDate.isVisible = false
                    layoutInfoEventLocation.isVisible = false
                    textInfoEventPoint.isVisible = false
                    val textInfoEventNameLayoutParams = textInfoEventName.layoutParams as ViewGroup.MarginLayoutParams
                    textInfoEventNameLayoutParams.setMargins(0, 0, 0, 0)
                    textInfoEventName.layoutParams = textInfoEventNameLayoutParams
                    removeAllSpan(textInfoEventName)
                    textInfoEventName.text = "오늘은 일정이 없는 날이에요"
                }
                EventType.HAS_ATTENDANCE -> {
                    layoutInfoEventDate.isVisible = true
                    textInfoEventDate.text = soptEvent.date
                    layoutInfoEventLocation.isVisible = true
                    textInfoEventLocation.text = soptEvent.location
                    textInfoEventPoint.isVisible = false
                    val textInfoEventNameLayoutParams = textInfoEventName.layoutParams as ViewGroup.MarginLayoutParams
                    textInfoEventNameLayoutParams.setMargins(0, 16.dpToPx(), 0, 0)
                    textInfoEventName.layoutParams = textInfoEventNameLayoutParams
                    removeAllSpan(textInfoEventName)
                    textInfoEventName.text = "오늘은 ${soptEvent.eventName} 날이에요"
                    (textInfoEventName.text as Spannable).run {
                        setSpan(StyleSpan(Typeface.BOLD), 4, 4 + (soptEvent.eventName.length), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
                EventType.NO_ATTENDANCE -> {
                    layoutInfoEventDate.isVisible = true
                    textInfoEventDate.text = soptEvent.date
                    layoutInfoEventLocation.isVisible = true
                    textInfoEventLocation.text = soptEvent.location
                    textInfoEventPoint.isVisible = true
                    val textInfoEventNameLayoutParams = textInfoEventName.layoutParams as ViewGroup.MarginLayoutParams
                    textInfoEventNameLayoutParams.setMargins(0, 16.dpToPx(), 0, 0)
                    textInfoEventName.layoutParams = textInfoEventNameLayoutParams
                    removeAllSpan(textInfoEventName)
                    textInfoEventName.text = "오늘은 ${soptEvent.eventName} 날이에요"
                    (textInfoEventName.text as Spannable).run {
                        setSpan(StyleSpan(Typeface.BOLD), 4, 4 + (soptEvent.eventName.length), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            }
        }
    }

    private fun updateAttendanceUserInfo(userInfo: AttendanceUserInfo) {
        attendanceAdapter.updateUserInfo(userInfo)
    }

    private fun updateAttendanceSummary(summary: AttendanceSummary) {
        attendanceAdapter.updateSummary(summary)
    }

    private fun updateAttendanceLog(log: List<AttendanceLog>) {
        attendanceAdapter.submitList(log)
    }

    private fun removeAllSpan(textView: TextView) {
        val originalText = textView.text
        (textView.text as Spannable).run {
            val spansToRemove = this.getSpans(0, this.length, Any::class.java)
            for (span in spansToRemove) {
                this.removeSpan(span)
            }
        }
        textView.text = originalText
    }
}