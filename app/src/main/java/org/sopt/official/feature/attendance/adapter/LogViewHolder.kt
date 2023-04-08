package org.sopt.official.feature.attendance.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.databinding.ItemAttendanceHistoryLogBinding
import org.sopt.official.domain.entity.attendance.AttendanceLog

class LogViewHolder(private val binding: ItemAttendanceHistoryLogBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(log: AttendanceLog) {
        initView(log)
    }

    private fun initView(log: AttendanceLog) {
        binding.run {
            textAttendanceEventName.text = log.eventName
            textAttendanceDate.text = log.date

            textAttendanceState.text = log.attendanceState
            when (log.attendanceState) {
                "ATTENDANCE" -> {
                    textAttendanceState.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#A7F8BE"))
                    textAttendanceState.setTextColor(ColorStateList.valueOf(Color.parseColor("#378F5C")))
                }
                "TARDY" -> {
                    textAttendanceState.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFEB80"))
                    textAttendanceState.setTextColor(ColorStateList.valueOf(Color.parseColor("#D09600")))
                }
                "ABSENT", "LEAVE_EARLY" -> {
                    textAttendanceState.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFD3D3"))
                    textAttendanceState.setTextColor(ColorStateList.valueOf(Color.parseColor("#E45656")))
                }
                else -> {
                    throw IllegalArgumentException("Illegal attendanceState argument: ${log.attendanceState}")
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): LogViewHolder {
            val binding = ItemAttendanceHistoryLogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return LogViewHolder(binding)
        }
    }
}