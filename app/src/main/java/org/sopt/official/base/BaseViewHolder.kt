package org.sopt.official.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder<ItemType, BindType, Binding : ViewBinding>(
    protected val binding: Binding
) : RecyclerView.ViewHolder(binding.root) {
    constructor(
        parent: ViewGroup,
        inflate: (LayoutInflater, ViewGroup?, Boolean) -> Binding,
    ) : this(inflate(LayoutInflater.from(parent.context), parent, false))

    internal fun onBindInternal(item: ItemType?, position: Int) {
        item?.let { onBind(it as BindType, position) }
    }

    protected open fun onBind(item: BindType, position: Int) = Unit
}