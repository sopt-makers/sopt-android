package org.sopt.official.feature.attendance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.R
import org.sopt.official.databinding.ItemAttendanceHistoryLogBinding
import org.sopt.official.domain.entity.attendance.AttendanceLog
import org.sopt.official.domain.entity.attendance.AttendanceStatus

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
                AttendanceStatus.ATTENDANCE.statusKorean -> {
                    textAttendanceState.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.sopt_green)
                    textAttendanceState.setTextColor(root.context.getColor(R.color.on_sopt_green))
                }
                AttendanceStatus.TARDY.statusKorean -> {
                    textAttendanceState.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.sopt_yellow)
                    textAttendanceState.setTextColor(root.context.getColor(R.color.on_sopt_yellow))
                }
                AttendanceStatus.ABSENT.statusKorean -> {
                    textAttendanceState.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.sopt_red)
                    textAttendanceState.setTextColor(root.context.getColor(R.color.on_sopt_red))
                }
                AttendanceStatus.PARTICIPATE.statusKorean -> {
                    textAttendanceState.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.gray_30)
                    textAttendanceState.setTextColor(root.context.getColor(R.color.gray_100))
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