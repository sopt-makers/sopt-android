package org.sopt.official.feature.attendance

import android.annotation.SuppressLint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.*
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.official.R
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
    private val menuProvider = (
        object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_attendance_overview, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_refresh -> {
                        attendanceViewModel.fetchSoptEvent()
                        attendanceViewModel.fetchAttendanceHistory()
                        true
                    }
                    else -> false
                }
            }
        }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
        fetchData()
        observeData()
    }

    private fun initView() {
        initToolbar()
        initRecyclerView()
        initListener()
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
        addMenuProvider(menuProvider, this)
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

    private fun initListener() {
        binding.btnAttendance.run {
            setOnClickListener {
                AttendanceCodeDialog(this.text.toString()).show(supportFragmentManager, "attendanceCodeDialog")
            }
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

    private fun observeAttendanceHistory() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                attendanceViewModel.attendanceHistory.collect { attendanceHistory ->
                    when (attendanceHistory) {
                        is AttendanceState.Success -> {
                            updateAttendanceUserInfo(attendanceHistory.data.userInfo)
                            updateAttendanceSummary(attendanceHistory.data.attendanceSummary)
                            updateAttendanceLog(
                                attendanceHistory.data.attendanceLog.filterNot {
                                    it.attribute == EventAttribute.ETC && it.attendanceState != AttendanceStatus.PARTICIPATE.statusKorean
                                }
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun updateSoptEventComponent(soptEvent: SoptEvent) {
        when (soptEvent.eventType) {
            EventType.NO_SESSION -> {
                updateSoptEventComponentWithNoSession()
            }

            EventType.HAS_ATTENDANCE -> {
                updateSoptEventComponentWithHasAttendance(soptEvent)
            }

            EventType.NO_ATTENDANCE -> {
                updateSoptEventComponentWithNoAttendance(soptEvent)
            }
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
            viewModel = attendanceViewModel
            layoutInfoEventDate.isVisible = true
            textInfoEventDate.text = soptEvent.date
            layoutInfoEventLocation.isVisible = true
            textInfoEventLocation.text = soptEvent.location
            textInfoEventPoint.isVisible = (soptEvent.message != "")
            textInfoEventPoint.text = soptEvent.message
            val textInfoEventNameLayoutParams = textInfoEventName.layoutParams as ViewGroup.MarginLayoutParams
            textInfoEventNameLayoutParams.setMargins(0, 16.dpToPx(), 0, 0)
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
                    tvAttendanceProgress1.text = soptEvent.attendances[0].attendedAt
                    tvAttendanceProgress2.text = "2차 출석"
                }
                2 -> {
                    tvAttendanceProgress1.text = soptEvent.attendances[0].attendedAt
                    tvAttendanceProgress2.text = soptEvent.attendances[1].attendedAt
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
            textInfoEventNameLayoutParams.setMargins(0, 16.dpToPx(), 0, 0)
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
            val spansToRemove = this.getSpans(0, this.length, Any::class.java)
            for (span in spansToRemove) {
                this.removeSpan(span)
            }
        }
        textView.text = originalText
    }

    private fun updateAttendanceUserInfo(userInfo: AttendanceUserInfo) {
        attendanceAdapter.updateUserInfo(userInfo)
    }

    private fun updateAttendanceSummary(summary: AttendanceSummary) {
        attendanceAdapter.updateSummary(summary)
    }

    private fun updateAttendanceLog(log: List<AttendanceLog>) {
        attendanceAdapter.submitList(log)
        binding.recyclerViewAttendanceHistory.scrollToPosition(0)
    }
}