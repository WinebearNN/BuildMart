package com.versaiilers.buildmart.data.datasource

import android.util.Log
import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Message
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Inject



class LocalDataSourceChat @Inject constructor(boxStore: BoxStore) {

    private val chatBox: Box<Chat> = boxStore.boxFor(Chat::class.java)
    private val messageBox: Box<Message> = boxStore.boxFor(Message::class.java)

    companion object {
        private const val TAG = "LocalDataSourceChat"
    }

    /**
     * Загружает все чаты из локальной базы данных.
     */
    fun getChats(): List<Chat> = chatBox.all

    /**
     * Сохраняет список сообщений в локальную базу данных.
     */
    fun saveMessages(messages: Collection<Message>) {
        try {
            messageBox.put(messages)
            Log.d(TAG, "Saved ${messages.size} messages.")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save messages", e)
        }
    }

    /**
     * Сохраняет чаты в локальную базу данных.
     */
    fun saveChats(chats: List<Chat>) {
        try {
            chatBox.put(chats)
            Log.d(TAG, "Saved ${chats.size} chats.")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save chats", e)
        }
    }

    fun saveMessage(message: Message) {
        saveMessages(listOf(message))
    }

    fun removeAllChats(){
        chatBox.removeAll()
    }

    fun removeChat(id:Long){
        chatBox.remove(id)
    }
    fun removeAllMessages(){
        messageBox.removeAll()
    }

    fun removeMessage(id:Long){
        messageBox.remove(id)
    }

    /**
     * Сохраняет список списков сообщений в локальную базу данных.
     */
    fun saveMessages(messageGroups: List<List<Message>>) {
        Log.d(TAG, "Attempting to save List<List<Message>>")
        val flatMessages = messageGroups.flatten()
        saveMessages(flatMessages)
    }
}