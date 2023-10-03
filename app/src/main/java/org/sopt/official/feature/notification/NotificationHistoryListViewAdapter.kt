package org.sopt.official.feature.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.R
import org.sopt.official.core.view.ItemDiffCallback
import org.sopt.official.databinding.ItemNotificationHistoryBinding
import org.sopt.official.domain.entity.notification.NotificationHistoryItem
import org.sopt.official.util.drawableOf

class NotificationHistoryListViewAdapter(
    private val clickListener: NotificationHistoryItemClickListener
) : ListAdapter<NotificationHistoryItem, NotificationHistoryListViewAdapter.ViewHolder>(
    ItemDiffCallback(
        onContentsTheSame = { old, new -> old.notificationId == new.notificationId },
        onItemsTheSame = { old, new -> old == new }
    )
) {
    private var _viewBinding: ItemNotificationHistoryBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun getItemCount(): Int = currentList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _viewBinding = ItemNotificationHistoryBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList[position].let { item ->
            holder.bind(item)
            holder.itemView.setOnClickListener {
                clickListener.onClickNotificationHistoryItem(position)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        _viewBinding = null
    }

    fun updateNotificationReadingState(position: Int) {
        val newList = currentList.toMutableList()
        newList[position].isRead = true
        submitList(newList)
        notifyItemChanged(position)
    }

    fun updateEntireNotificationReadingState() {
        val newList = currentList.toMutableList()
        for (notification in newList) {
            notification.isRead = true
        }
        submitList(newList)
        notifyItemRangeChanged(0, newList.size)
    }


    inner class ViewHolder(
        private val viewBinding: ItemNotificationHistoryBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: NotificationHistoryItem) {
            viewBinding.apply {
                textViewTitle.text = item.title
                textViewContent.text = item.content
                textViewReceivedTime.text = item.createdAt
                constraintLayoutBackground.background = when (item.isRead) {
                    true -> root.context.drawableOf(R.color.black_100)
                    false -> root.context.drawableOf(R.color.black_80)
                }
            }
        }
    }
}