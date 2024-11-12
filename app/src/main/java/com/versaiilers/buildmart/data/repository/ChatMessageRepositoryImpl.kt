package com.versaiilers.buildmart.data.repository

import android.util.Log
import com.versaiilers.buildmart.data.datasource.chat.LocalDataSourceChat
import com.versaiilers.buildmart.data.datasource.chat.RemoteDataSourceChat
import com.versaiilers.buildmart.data.datasource.message.LocalDataSourceMessage
import com.versaiilers.buildmart.data.datasource.message.RemoteDataSourceMessage
import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Message
import com.versaiilers.buildmart.domain.repository.ChatMessageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatMessageRepositoryImpl @Inject constructor(
    private val localDataSourceChat: LocalDataSourceChat,
    private val remoteDataSourceChat: RemoteDataSourceChat,
    private val localDataSourceMessage: LocalDataSourceMessage,
    private val remoteDataSourceMessage: RemoteDataSourceMessage
) : ChatMessageRepository {

    companion object {
        private const val TAG = "ChatMessageRepositoryImpl"
    }

    override suspend fun sendMessageOverWebSocket(message: Message):Result<Unit>{
        return remoteDataSourceMessage.sendMessageOverWebSocket(message)
    }

    // Получаем локальные чаты сначала
    override suspend fun getAllChatsLocal(userGlobalId: String): Result<List<Chat>> {
        val localChats = localDataSourceChat.getChats()
        return if (localChats.isNotEmpty()) {
            Result.success(localChats)  // Сразу возвращаем локальные чаты
        } else {
            Log.d(TAG, "Локальные чаты пусты, загружаем с сервера")
            refreshChats(userGlobalId)  // Загружаем с сервера и обновляем локальное хранилище
        }
    }

    override suspend fun saveMessageLocal(message:Message){
        return localDataSourceMessage.saveMessage(message)
    }
    override suspend fun getAllChatsRemote(userGlobalId: String): Result<List<Chat>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllMessagesLocal(chatGlobalId: Long): Result<List<Message>> {
        val localMessages = localDataSourceMessage.getMessages(chatGlobalId)
        return if (localMessages.isNotEmpty()) {
            Result.success(localMessages)  // Сразу возвращаем локальные чаты
        } else {
            Log.d(TAG, "Локальных сообщений нет в этом чате ${chatGlobalId}, загружаем с сервера")
            refreshMessages(chatGlobalId)  // Загружаем с сервера и обновляем локальное хранилище
        }
    }

    suspend fun refreshMessages(chatGlobalId: Long):Result<List<Message>> {
        return try {
            remoteDataSourceMessage.getAllMessages(chatGlobalId).also{result->
                result.getOrNull()?.let{messages ->
                    saveToLocalMessages(messages)
                }
            }.map{it}
        }catch (e:Exception){
            Log.e(TAG,"Ошибка обновления сообщений в чате ${chatGlobalId}")
            Result.failure(e)
        }
    }


    // Получаем серверные чаты и обновляем локальное хранилище
    override suspend fun refreshChats(userGlobalId: String): Result<List<Chat>> {
        return try {
            remoteDataSourceChat.getAllChats(userGlobalId).also { result ->
                result.getOrNull()?.let { (chats, messages) ->
                    saveToLocalChats(chats, messages)

                }
            }.map { it.first }  // Возвращаем только список чатов
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка обновления чатов: ${e.message}")
            Result.failure(e)
        }
    }

    private fun saveToLocalChats(chats: List<Chat>, messages: List<List<Message>>) {
        localDataSourceChat.saveChats(chats)
        localDataSourceMessage.saveMessages(messages)
        Log.d(TAG, "Данные успешно сохранены в локальное хранилище")
    }

    private fun saveToLocalMessages(messages:List<Message>){
        localDataSourceMessage.saveMessages(messages)
    }

    // ChatRepositoryImpl.kt
    fun connectWebSocket(onNewMessage: (Message) -> Unit) {
        remoteDataSourceChat.connectWebSocket { message ->
            // Сохраняем новое сообщение в локальное хранилище
            localDataSourceMessage.saveMessages(listOf(message))

            // Передаем новое сообщение дальше для обработки в ViewModel или фрагменте
            onNewMessage(message)
        }
    }

    // Отключение от WebSocket
    fun disconnectWebSocket(): Result<Unit> {
        return try {
            remoteDataSourceChat.disconnectWebSocket()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка отключения WebSocket: ${e.message}")
            Result.failure(e)
        }
    }
}