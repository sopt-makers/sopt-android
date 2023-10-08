package org.sopt.official.feature.notification

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.R
import org.sopt.official.common.view.ItemDiffCallback
import org.sopt.official.databinding.ItemNotificationHistoryBinding
import org.sopt.official.domain.entity.notification.NotificationHistoryItem
import org.sopt.official.util.drawableOf
import java.util.Date
import java.util.Locale

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

    fun updateNotificationHistoryList(newList: List<NotificationHistoryItem>) {
        submitList(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val viewBinding: ItemNotificationHistoryBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: NotificationHistoryItem) {
            viewBinding.apply {
                textViewTitle.text = item.title
                textViewContent.text = item.content
                textViewReceivedTime.text = item.createdAt.convertToTimesAgo()
                constraintLayoutBackground.background = when (item.isRead) {
                    true -> root.context.drawableOf(R.color.black_100)
                    false -> root.context.drawableOf(R.color.black_80)
                }
            }
        }

        private fun String.convertToTimesAgo(): String {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.KOREA)
            dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            val currentDate = Date()
            val receivedDate = dateFormat.parse(this)
            val diffInMillis = currentDate.time - receivedDate.time

            val diffInDays = diffInMillis / ONE_DAY_IN_MILLISECONDS
            val diffInHours = diffInMillis / ONE_HOUR_IN_MILLISECONDS
            val diffInMinutes = diffInMillis / ONE_MINUTE_IN_MILLISECONDS

            return when {
                diffInDays >= 1 -> "${diffInDays}일 전"
                diffInHours >= 1 -> "${diffInHours}시간 전"
                diffInMinutes >= 1 -> "${diffInMinutes}분 전"
                else -> "방금"
            }
        }
    }

    companion object {
        const val ONE_DAY_IN_MILLISECONDS = 86400000L
        const val ONE_HOUR_IN_MILLISECONDS = 3600000L
        const val ONE_MINUTE_IN_MILLISECONDS = 60000L
    }
}