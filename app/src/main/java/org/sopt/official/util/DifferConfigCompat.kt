package org.sopt.official.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import org.sopt.official.base.BaseItemType

class DifferConfigCompat<T: BaseItemType> : DiffUtil.ItemCallback<T>() {
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}