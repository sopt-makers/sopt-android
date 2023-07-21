package org.sopt.official.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import org.sopt.official.util.DifferConfigCompat

abstract class BaseListAdapter<ItemType : BaseItemType, HolderType : BaseViewHolder<ItemType, *, *>>(
    differ: DiffUtil.ItemCallback<ItemType> = DifferConfigCompat()
) : ListAdapter<ItemType, HolderType>(differ) {

    override fun onBindViewHolder(holder: HolderType, position: Int) {
        holder.onBindInternal(getItem(position), position)
    }

    override fun onBindViewHolder(holder: HolderType, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        holder.onBindInternal(getItem(position), position)
    }
}

abstract class BaseAdapter<ItemType : BaseItemType>(
    differ: DiffUtil.ItemCallback<ItemType> = DifferConfigCompat()
) : BaseListAdapter<ItemType, BaseViewHolder<ItemType, *, *>>(differ)