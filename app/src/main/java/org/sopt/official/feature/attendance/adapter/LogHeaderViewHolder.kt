package org.sopt.official.feature.attendance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.databinding.ItemAttendanceHistoryLogHeaderBinding

class LogHeaderViewHolder(
    binding: ItemAttendanceHistoryLogHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): LogHeaderViewHolder {
            val binding = ItemAttendanceHistoryLogHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return LogHeaderViewHolder(binding)
        }
    }
}