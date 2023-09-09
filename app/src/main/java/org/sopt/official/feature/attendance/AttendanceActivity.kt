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
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.R
import org.sopt.official.core.di.toast
import org.sopt.official.databinding.ActivityAttendanceBinding
import org.sopt.official.domain.entity.attendance.AttendanceLog
import org.sopt.official.domain.entity.attendance.AttendanceStatus
import org.sopt.official.domain.entity.attendance.AttendanceSummary
import org.sopt.official.domain.entity.attendance.AttendanceUserInfo
import org.sopt.official.domain.entity.attendance.EventType
import org.sopt.official.domain.entity.attendance.SoptEvent
import org.sopt.official.feature.attendance.adapter.AttendanceAdapter
import org.sopt.official.feature.attendance.model.AttendanceState
import org.sopt.official.util.dp

@AndroidEntryPoint
class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceBinding
    private val attendanceViewModel by viewModels<AttendanceViewModel>()
    private lateinit var attendanceAdapter: AttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = attendanceViewModel
        binding.lifecycleOwner = this

        initView()
        initUiInteraction()
        initListener()
        fetchData()
        observeData()
    }

    private fun initView() {
        initToolbar()
        initRecyclerView()
        initListener()
    }

    private fun initUiInteraction() {
        binding.icRefresh.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_rotation))
            fetchData()
        }
    }

    private fun fetchData() {
        attendanceViewModel.run {
            fetchSoptEvent()
            fetchAttendanceHistory()
        }
    }

    private fun observeData() {
        observeSoptEvent()
        observeAttendanceHistory()
    }

    private fun initToolbar() {
        binding.toolbar.run {
            setSupportActionBar(this)
            setNavigationOnClickListener { this@AttendanceActivity.finish() }
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
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
                            outRect.set(24.dp, 32.dp, 24.dp, 12.dp)
                        }

                        attendanceAdapter.itemCount - 1 -> {
                            outRect.set(24, 12.dp, 24.dp, 32.dp)
                        }

                        else -> {
                            outRect.set(24.dp, 12.dp, 24.dp, 12.dp)
                        }
                    }
                }
            })
        }
    }

    private fun initListener() {
        binding.btnAttendance.setOnClickListener {
            AttendanceCodeDialog()
                .setTitle(binding.btnAttendance.text.toString())
                .show(supportFragmentManager, "attendanceCodeDialog")
        }
    }

    private fun observeSoptEvent() {
        attendanceViewModel.soptEvent
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is AttendanceState.Success -> updateSoptEventComponent(it.data)
                    is AttendanceState.Failure -> toast("문제가 발생했습니다")
                    else -> {}
                }
            }.launchIn(lifecycleScope)
    }

    private fun observeAttendanceHistory() {
        attendanceViewModel.attendanceHistory
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is AttendanceState.Success -> {
                        updateAttendanceUserInfo(it.data.userInfo)
                        updateAttendanceSummary(it.data.attendanceSummary)
                        updateAttendanceLog(it.data.attendanceLog)
                    }

                    is AttendanceState.Failure -> {
                        Toast.makeText(this@AttendanceActivity, "문제가 발생했습니다", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }.launchIn(lifecycleScope)
    }

    private fun updateSoptEventComponent(soptEvent: SoptEvent) {
        when (soptEvent.eventType) {
            EventType.NO_SESSION -> updateSoptEventComponentWithNoSession()
            EventType.HAS_ATTENDANCE -> updateSoptEventComponentWithHasAttendance(soptEvent)
            EventType.NO_ATTENDANCE -> updateSoptEventComponentWithNoAttendance(soptEvent)
        }
    }

    private fun updateSoptEventComponentWithNoSession() {
        binding.run {
            layoutInfoEventDate.isVisible = false
            layoutInfoEventLocation.isVisible = false
            textInfoEventPoint.isVisible = false
            val textInfoEventNameLayoutParams = textInfoEventName.layoutParams as ViewGroup.MarginLayoutParams
            textInfoEventNameLayoutParams.setMargins(0, 0, 0, 0)
            textInfoEventName.layoutParams = textInfoEventNameLayoutParams
            removeAllSpan(textInfoEventName)
            textInfoEventName.text = "오늘은 일정이 없는 날이에요"
            layoutAttendanceProgress.isVisible = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateSoptEventComponentWithHasAttendance(soptEvent: SoptEvent) {
        binding.run {
            layoutInfoEventDate.isVisible = true
            textInfoEventDate.text = soptEvent.date
            layoutInfoEventLocation.isVisible = true
            textInfoEventLocation.text = soptEvent.location
            textInfoEventPoint.isVisible = (soptEvent.message != "")
            textInfoEventPoint.text = soptEvent.message
            val textInfoEventNameLayoutParams = textInfoEventName.layoutParams as ViewGroup.MarginLayoutParams
            textInfoEventNameLayoutParams.setMargins(0, 16.dp, 0, 0)
            textInfoEventName.layoutParams = textInfoEventNameLayoutParams
            removeAllSpan(textInfoEventName)
            textInfoEventName.text = "오늘은 ${soptEvent.eventName} 날이에요"
            (textInfoEventName.text as Spannable).run {
                setSpan(StyleSpan(Typeface.BOLD), 4, 4 + (soptEvent.eventName.length), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            layoutAttendanceProgress.isVisible = true
            attendanceViewModel.setProgressBar(soptEvent)
            when (soptEvent.attendances.size) {
                1 -> {
                    tvAttendanceProgress1.text = if (soptEvent.attendances[0].status == AttendanceStatus.ATTENDANCE) {
                        soptEvent.attendances[0].attendedAt
                    } else {
                        "결석"
                    }
                    tvAttendanceProgress2.text = "2차 출석"
                }

                2 -> {
                    tvAttendanceProgress1.text = if (soptEvent.attendances[0].status == AttendanceStatus.ATTENDANCE) {
                        soptEvent.attendances[0].attendedAt
                    } else {
                        "결석"
                    }
                    tvAttendanceProgress2.text = if (soptEvent.attendances[1].status == AttendanceStatus.ATTENDANCE) {
                        soptEvent.attendances[1].attendedAt
                    } else {
                        "결석"
                    }
                }

                else -> {
                    tvAttendanceProgress1.text = "1차 출석"
                    tvAttendanceProgress2.text = "2차 출석"
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateSoptEventComponentWithNoAttendance(soptEvent: SoptEvent) {
        binding.run {
            layoutInfoEventDate.isVisible = true
            textInfoEventDate.text = soptEvent.date
            layoutInfoEventLocation.isVisible = true
            textInfoEventLocation.text = soptEvent.location
            textInfoEventPoint.isVisible = (soptEvent.message != "")
            textInfoEventPoint.text = soptEvent.message
            val textInfoEventNameLayoutParams = textInfoEventName.layoutParams as ViewGroup.MarginLayoutParams
            textInfoEventNameLayoutParams.setMargins(0, 16.dp, 0, 0)
            textInfoEventName.layoutParams = textInfoEventNameLayoutParams
            removeAllSpan(textInfoEventName)
            textInfoEventName.text = "오늘은 ${soptEvent.eventName} 날이에요"
            (textInfoEventName.text as Spannable).run {
                setSpan(StyleSpan(Typeface.BOLD), 4, 4 + (soptEvent.eventName.length), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            layoutAttendanceProgress.isVisible = false
        }
    }

    private fun removeAllSpan(textView: TextView) {
        val originalText = textView.text
        (textView.text as Spannable).run {
            getSpans(0, this.length, Any::class.java).forEach { this.removeSpan(it) }
        }
        textView.text = originalText
    }

    private fun updateAttendanceUserInfo(userInfo: AttendanceUserInfo) {
        attendanceAdapter.updateUserInfo(userInfo)
    }

    private fun updateAttendanceSummary(summary: AttendanceSummary) {
        attendanceAdapter.updateSummary(summary)
    }

    private fun updateAttendanceLog(log: List<AttendanceLog?>) {
        val list = log.toMutableList().apply {
            repeat(3) { add(0, null) }
        }
        attendanceAdapter.submitList(list)
        binding.recyclerViewAttendanceHistory.scrollToPosition(0)
    }
}