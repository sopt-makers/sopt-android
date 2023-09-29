package org.sopt.official.feature.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse
import org.sopt.official.databinding.ItemNotificationHistoryBinding

class NotificationHistoryRecyclerViewAdapter(
    private var notificationList: List<NotificationHistoryItemResponse>,
    private val clickListener: NotificationHistoryItemClickListener
) : RecyclerView.Adapter<NotificationHistoryRecyclerViewAdapter.NotificationHistoryViewHolder>() {

    private var _viewBinding: ItemNotificationHistoryBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun getItemCount(): Int = notificationList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHistoryViewHolder {
        _viewBinding = ItemNotificationHistoryBinding.inflate(LayoutInflater.from(parent.context))
        return NotificationHistoryViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: NotificationHistoryViewHolder, position: Int) {
        notificationList[position].let { item ->
            holder.bind(item)
            holder.itemView.setOnClickListener {
                clickListener.onClickNotificationHistoryItem(item)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: NotificationHistoryViewHolder) {
        super.onViewDetachedFromWindow(holder)
        _viewBinding = null
    }


    inner class NotificationHistoryViewHolder(
        private val viewBinding: ItemNotificationHistoryBinding
    ): RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: NotificationHistoryItemResponse) {
            viewBinding.apply {
                textViewTitle.text = item.title
                textViewContent.text = item.content
                textViewReceivedTime.text = item.createdAt
            }

            // TODO: Set background and divider color.
            when (item.type) {
            }
        }
    }
}