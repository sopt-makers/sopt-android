package org.sopt.official.feature.poke.message_bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.domain.poke.entity.PokeMessageList
import org.sopt.official.feature.poke.databinding.ItemMessageBinding

class MessageListRecyclerAdapter(
    private val messageList: List<PokeMessageList.PokeMessage>,
    private val clickListener: MessageItemClickListener,
) : RecyclerView.Adapter<MessageListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListRecyclerAdapter.ViewHolder {
        return ViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int = messageList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(messageList[position])
        holder.itemView.setOnClickListener {
            clickListener.onClickMessageItem(messageList[position].content)
        }
    }

    inner class ViewHolder(
        private val binding: ItemMessageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: PokeMessageList.PokeMessage) {
            binding.textViewMessage.text = item.content
        }
    }
}