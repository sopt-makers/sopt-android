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
import org.sopt.official.type.SoptColors

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
                    (ForegroundColorSpan(ContextCompat.getColor(textUserAttendancePoint.context, SoptColors.orange_100))),
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
