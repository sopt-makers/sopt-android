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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.databinding.ItemAttendanceHistoryLogBinding
import org.sopt.official.domain.entity.attendance.AttendanceLog
import org.sopt.official.domain.entity.attendance.AttendanceStatus
import org.sopt.official.type.SoptColors

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
                    textAttendanceState.backgroundTintList = ContextCompat.getColorStateList(root.context, SoptColors.sopt_green)
                    textAttendanceState.setTextColor(root.context.getColor(SoptColors.on_sopt_green))
                }

                AttendanceStatus.TARDY.statusKorean -> {
                    textAttendanceState.backgroundTintList = ContextCompat.getColorStateList(root.context, SoptColors.sopt_yellow)
                    textAttendanceState.setTextColor(root.context.getColor(SoptColors.on_sopt_yellow))
                }

                AttendanceStatus.ABSENT.statusKorean -> {
                    textAttendanceState.backgroundTintList = ContextCompat.getColorStateList(root.context, SoptColors.sopt_red)
                    textAttendanceState.setTextColor(root.context.getColor(SoptColors.on_sopt_red))
                }

                AttendanceStatus.PARTICIPATE.statusKorean -> {
                    textAttendanceState.backgroundTintList = ContextCompat.getColorStateList(root.context, SoptColors.gray_30)
                    textAttendanceState.setTextColor(root.context.getColor(SoptColors.gray_100))
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
