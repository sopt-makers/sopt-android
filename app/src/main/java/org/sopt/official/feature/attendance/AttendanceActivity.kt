/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.attendance

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.deeplinkdispatch.DeepLink
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.R
import org.sopt.official.common.util.colorOf
import org.sopt.official.common.util.dp
import org.sopt.official.common.util.stringOf
import org.sopt.official.common.view.toast
import org.sopt.official.databinding.ActivityAttendanceBinding
import org.sopt.official.common.di.SoptViewModelFactory
import org.sopt.official.domain.entity.attendance.AttendanceLog
import org.sopt.official.domain.entity.attendance.AttendanceStatus
import org.sopt.official.domain.entity.attendance.AttendanceSummary
import org.sopt.official.domain.entity.attendance.AttendanceUserInfo
import org.sopt.official.domain.entity.attendance.EventType
import org.sopt.official.domain.entity.attendance.SoptEvent
import org.sopt.official.feature.attendance.adapter.AttendanceAdapter
import org.sopt.official.feature.attendance.model.AttendanceState
import org.sopt.official.type.SoptColors

@DeepLink("sopt://attendance")
class AttendanceActivity(
    private val viewModelFactory: SoptViewModelFactory
) : AppCompatActivity() {

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = viewModelFactory

    private lateinit var binding: ActivityAttendanceBinding
    private val viewModel by viewModels<AttendanceViewModel>()
    private lateinit var attendanceAdapter: AttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initUiInteraction()
        initListener()
        observeData()
        observeProgressState()
    }

    private fun observeProgressState() {
        viewModel.isFirstToSecondLineActive.observe(this) {
            binding.lineFirstToSecondActive.isInvisible = !it
        }
        viewModel.isSecondToThirdLineActive.observe(this) {
            binding.lineSecondToThirdActive.isInvisible = !it
        }
        viewModel.isFirstProgressBarAttendance.observe(this) {
            binding.ivAttendanceProgress1Check.setImageResource(
                if (it) R.drawable.ic_attendance_check_gray else R.drawable.ic_attendance_close_gray
            )
        }
        viewModel.isFirstProgressBarActive.observe(this) {
            binding.ivAttendanceProgress1Check.isInvisible = !it
            binding.tvAttendanceProgress1.setTextColor(
                if (it) colorOf(SoptColors.mds_gray_10) else colorOf(SoptColors.mds_gray_500)
            )
        }
        viewModel.isSecondProgressBarAttendance.observe(this) {
            binding.ivAttendanceProgress2Check.setImageResource(
                if (it) R.drawable.ic_attendance_check_gray else R.drawable.ic_attendance_close_gray
            )
        }
        viewModel.isSecondProgressBarActive.observe(this) {
            binding.ivAttendanceProgress2Check.isInvisible = !it
            binding.tvAttendanceProgress2.setTextColor(
                if (it) colorOf(SoptColors.mds_gray_10) else colorOf(SoptColors.mds_gray_500)
            )
        }
        viewModel.isThirdProgressBarVisible.observe(this) {
            binding.ivAttendanceProgress3Tardy.isInvisible = !it
            binding.ivAttendanceProgress3Attendance.isInvisible = it
            binding.tvAttendanceProgress3.text = stringOf(
                if (it) R.string.attendance_progress_third_tardy else R.string.attendance_progress_third_complete
            )
        }
        viewModel.isThirdProgressBarActive.observe(this) {
            binding.ivAttendanceProgress3Empty.isInvisible = it
            binding.tvAttendanceProgress3Attendance.text = stringOf(
                if (it) R.string.attendance_progress_third_absent else R.string.attendance_progress_before
            )
            binding.tvAttendanceProgress3Attendance.setTextColor(
                if (it) colorOf(SoptColors.mds_gray_10) else colorOf(SoptColors.mds_gray_500)
            )
        }
        viewModel.isThirdProgressBarBeforeAttendance.observe(this) {
            binding.ivAttendanceProgress3Attendance.setImageResource(
                if (it) R.drawable.ic_attendacne_check_white else R.drawable.ic_attendance_close_white
            )
        }
        viewModel.isThirdProgressBarActiveAndBeforeAttendance.observe(this) {
            binding.tvAttendanceProgress3.isInvisible = !it
            binding.tvAttendanceProgress3Attendance.isInvisible = it
        }
        viewModel.isAttendanceButtonEnabled.observe(this) {
            binding.btnAttendance.isEnabled = it
        }
        viewModel.attendanceButtonText.observe(this) {
            binding.btnAttendance.text = it
        }
        viewModel.isAttendanceButtonVisibility.observe(this) {
            binding.btnAttendance.isVisible = it
        }
    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutAttendance) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,   // ← statusBarPadding()
                systemBars.right,
                systemBars.bottom // ← navigationBarPadding() 대응
            )
            WindowInsetsCompat.CONSUMED
        }

        initToolbar()
        initRecyclerView()
        initListener()
    }

    private fun initUiInteraction() {
        binding.icRefresh.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_rotation))
            viewModel.fetchData()
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
                            outRect.set(24.dp, 12.dp, 24.dp, 32.dp)
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
            AttendanceCodeDialog().setTitle(binding.btnAttendance.text.toString()).show(supportFragmentManager, "attendanceCodeDialog")
        }
    }

    private fun observeSoptEvent() {
        viewModel.soptEvent.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is AttendanceState.Success -> updateSoptEventComponent(it.data)
                is AttendanceState.Failure -> toast("문제가 발생했습니다")
                else -> {}
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeAttendanceHistory() {
        viewModel.attendanceHistory.flowWithLifecycle(lifecycle).onEach {
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
            viewModel.setProgressBar(soptEvent)
            when (soptEvent.attendances.size) {
                1 -> {
                    tvAttendanceProgress1.text = if (soptEvent.attendances[0].status == AttendanceStatus.ATTENDANCE) {
                        soptEvent.attendances[0].attendedAt
                    } else {
                        "-"
                    }
                    tvAttendanceProgress2.text = "2차 출석"
                }

                2 -> {
                    tvAttendanceProgress1.text = if (soptEvent.attendances[0].status == AttendanceStatus.ATTENDANCE) {
                        soptEvent.attendances[0].attendedAt
                    } else {
                        "-"
                    }
                    tvAttendanceProgress2.text = if (soptEvent.attendances[1].status == AttendanceStatus.ATTENDANCE) {
                        soptEvent.attendances[1].attendedAt
                    } else {
                        "-"
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

    companion object {
        fun newInstance(context: Context) = Intent(context, AttendanceActivity::class.java)
    }
}