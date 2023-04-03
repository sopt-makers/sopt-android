package org.sopt.official.feature.attendance.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.databinding.ItemAttendanceHistorySummaryBinding
import org.sopt.official.domain.entity.attendance.AttendanceSummary

class SummaryViewHolder(private val binding: ItemAttendanceHistorySummaryBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(summary: AttendanceSummary) {
        initView(summary)
    }

    @SuppressLint("SetTextI18n")
    private fun initView(summary: AttendanceSummary) {
        binding.run {
            textAttendanceCountAll.text = "${summary.all}회"
            textAttendanceCountNormal.text = "${summary.normal}회"
            textAttendanceCountLate.text = "${summary.late}회"
            textAttendanceCountAbnormal.text = "${summary.abnormal}회"
        }
    }

    companion object {
        fun create(parent: ViewGroup): SummaryViewHolder {
            val binding = ItemAttendanceHistorySummaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return SummaryViewHolder(binding)
        }
    }
}