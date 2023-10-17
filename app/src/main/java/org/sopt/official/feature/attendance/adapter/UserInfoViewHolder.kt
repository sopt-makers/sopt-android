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
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.Spannable
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.R
import org.sopt.official.databinding.ItemAttendanceHistoryUserInfoBinding
import org.sopt.official.domain.entity.attendance.AttendanceUserInfo

class UserInfoViewHolder(private val binding: ItemAttendanceHistoryUserInfoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(userInfo: AttendanceUserInfo) {
        initView(userInfo)
    }

    @SuppressLint("SetTextI18n")
    private fun initView(userInfo: AttendanceUserInfo) {
        binding.run {
            textUserInfo.text = "${userInfo.generation}기 ${userInfo.partName}파트 ${userInfo.userName}"
            textUserAttendancePoint.text = "현재 출석점수는 ${userInfo.attendancePoint}점 입니다!"
            (textUserAttendancePoint.text as Spannable).run {
                setSpan(
                    (ForegroundColorSpan(ContextCompat.getColor(textUserAttendancePoint.context, R.color.orange_100))),
                    9,
                    9 + "${userInfo.attendancePoint}".length + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(StyleSpan(Typeface.BOLD), 9, 9 + "${userInfo.attendancePoint}".length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    companion object {
        private const val ATTENDANCE_RULE_URL = "https://sopt.org/rules"

        fun create(parent: ViewGroup): UserInfoViewHolder {
            val binding = ItemAttendanceHistoryUserInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            binding.imageAttendancePointInfo.setOnClickListener {
                Toast.makeText(parent.context, "제2장 제10조(출석)를 확인해주세요", Toast.LENGTH_SHORT).show()
                Intent(Intent.ACTION_VIEW, Uri.parse(ATTENDANCE_RULE_URL)).run {
                    parent.context.startActivity(this)
                }
            }

            return UserInfoViewHolder(binding)
        }
    }
}
