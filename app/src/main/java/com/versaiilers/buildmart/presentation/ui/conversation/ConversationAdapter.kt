package com.versaiilers.buildmart.presentation.ui.conversation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.versaiilers.buildmart.databinding.ItemMessageLeftBinding
import com.versaiilers.buildmart.databinding.ItemMessageRightBinding
import com.versaiilers.buildmart.domain.entity.Message

class ConversationAdapter(private val userGlobalId: Long) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "ConversationAdapter"
        private const val VIEW_TYPE_LEFT = 0
        private const val VIEW_TYPE_RIGHT = 1
    }

    private val messages = mutableListOf<Message>()

    fun submitList(newMessages: List<Message>) {
        Log.d(TAG, "Messages received when calling submitList $newMessages")
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].userGlobalId == userGlobalId) VIEW_TYPE_RIGHT else VIEW_TYPE_LEFT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_RIGHT) {
            RightMessageViewHolder(ItemMessageRightBinding.inflate(inflater, parent, false))
        } else {
            LeftMessageViewHolder(ItemMessageLeftBinding.inflate(inflater, parent, false))

//            LeftMessageViewHolder(ItemMessageLeftBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is LeftMessageViewHolder) {
            holder.bind(message)
        } else if (holder is RightMessageViewHolder) {
            holder.bind(message)
        } else {
            throw Exception("ConversationAdapter: Incorrect ViewHolder type")
        }
    }

    override fun getItemCount(): Int = messages.size

    inner class LeftMessageViewHolder(private val binding: ItemMessageLeftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.itemMessageLeftTextView.text = message.content
            Log.d(TAG,"Для сообщения ${message.content} был использован левый")

        }
    }

    inner class RightMessageViewHolder(private val binding: ItemMessageRightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.itemMessageRightTextView.text = message.content
            Log.d(TAG,"Для сообщения ${message.content} был использован правый")
        }
    }
}


//        app:layout_constraintHorizontal_bias="1.0"
