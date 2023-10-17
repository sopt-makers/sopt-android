/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
