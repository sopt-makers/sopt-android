/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.domain.poke.entity.PokeMessageList
import org.sopt.official.feature.poke.databinding.ItemMessageBinding

class MessageListRecyclerAdapter(
    private val messageList: List<PokeMessageList.PokeMessage>,
    private val clickListener: MessageItemClickListener,
) : RecyclerView.Adapter<MessageListRecyclerAdapter.ViewHolder>() {
    private var lastClickTime: Long = 0
    private val clickInterval: Long = 1000 // 1초 동안 연속 클릭 무시

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            onBind(messageList[position])
            itemView.setOnClickListener {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime >= clickInterval) {
                    lastClickTime = currentTime
                    clickListener.onClickMessageItem(messageList[position].content)
                }
            }
        }
    }

    class ViewHolder(
        private val binding: ItemMessageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: PokeMessageList.PokeMessage) {
            binding.textViewMessage.text = item.content
        }
    }
}
