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
