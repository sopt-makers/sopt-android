/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            textAttendanceCountNormal.text = "${summary.normal}회"
            textAttendanceCountLate.text = "${summary.late}회"
            textAttendanceCountAbnormal.text = "${summary.abnormal}회"
            textAttendanceCountParticipate.text = "${summary.participate}회"
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
