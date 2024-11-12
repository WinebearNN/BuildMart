package com.versaiilers.buildmart.data.datasource.message

import android.util.Log
import com.versaiilers.buildmart.domain.entity.Message
import com.versaiilers.buildmart.domain.entity.Message_
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.exception.UniqueViolationException
import io.objectbox.query.QueryBuilder
import javax.inject.Inject

class LocalDataSourceMessage @Inject constructor(boxStore: BoxStore) {
    private val messageBox: Box<Message> = boxStore.boxFor(Message::class.java)

    companion object {
        private const val TAG = "LocalDataSourceMessage"
    }



    /**
     * Saves a list of messages to the local database.
     */
    fun saveMessages(messages: Collection<Message>) {
        if (messages.isEmpty()) {
            Log.w(TAG, "Attempted to save an empty message list.")
            return
        }
        try {
            for (message:Message in messages){
                saveMessage(message)
            }
            Log.d(TAG, "Saved ${messages.size} messages.")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save messages", e)
        }
    }


    fun getMessages(chatGlobalId: Long): List<Message> {
        Log.d(TAG,"Все сообщения, хранящиеся в messageBox: ${messageBox.all.toString()}")
        return messageBox.query()
            .equal(Message_.chatGlobalId, chatGlobalId)
            .build()
            .find()
    }
    /**
     * Save a single message.
     */
    fun saveMessage(message: Message) {
        val existingMessage = messageBox.query()
            .equal(Message_.globalId, message.globalId)
            .build()
            .findFirst()

        if (existingMessage == null) {
            messageBox.put(message)
        }
    }

    fun removeAllMessages() = messageBox.removeAll()

    fun removeMessage(id: Long) = messageBox.remove(id)

    /**
     * Saves a list of message lists to the local database.
     */
    fun saveMessages(messageGroups: List<List<Message>>) {
        val flatMessages = messageGroups.flatten()
        saveMessages(flatMessages)
    }

}