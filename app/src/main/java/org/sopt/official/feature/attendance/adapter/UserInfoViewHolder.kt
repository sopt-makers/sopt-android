package org.sopt.official.feature.attendance.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
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
                    (ForegroundColorSpan(ContextCompat.getColor(textUserAttendancePoint.context, R.color.purple_40))),
                    9,
                    9 + "${userInfo.attendancePoint}".length + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(StyleSpan(Typeface.BOLD), 9, 9 + "${userInfo.attendancePoint}".length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): UserInfoViewHolder {
            val binding = ItemAttendanceHistoryUserInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return UserInfoViewHolder(binding)
        }
    }
}