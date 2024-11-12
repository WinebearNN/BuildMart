package com.versaiilers.buildmart.data.datasource.chat

import android.util.Log
import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Chat_
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Inject


class LocalDataSourceChat @Inject constructor(boxStore: BoxStore) {

    private val chatBox: Box<Chat> = boxStore.boxFor(Chat::class.java)

    companion object {
        private const val TAG = "LocalDataSourceChat"
    }

    /**
     * Loads all chats from the local database.
     */
    fun getChats(): List<Chat> = chatBox.all


    /**
     * Saves chats to the local database.
     */
    fun saveChats(chats: List<Chat>) {
        if (chats.isEmpty()) {
            Log.w(TAG, "Attempted to save an empty chat list.")
            return
        }
        try {
            for (chat: Chat in chats) {
                saveChat(chat)
            }
            Log.d(TAG, "Saved ${chats.size} chats.")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save chats", e)
        }
    }

    private fun saveChat(chat: Chat) {
        val existingChat = chatBox.query()
            .equal(Chat_.globalId, chat.globalId)
            .build()
            .findFirst()

        if (existingChat == null) {
            chatBox.put(chat)
        } else {
            existingChat.lastMessage = chat.lastMessage
            chatBox.put(existingChat)
        }
    }


    fun removeAllChats() = chatBox.removeAll()

    fun removeChat(id: Long) = chatBox.remove(id)


}