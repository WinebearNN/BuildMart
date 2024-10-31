package com.versaiilers.buildmart.data.repository

import android.util.Log
import com.versaiilers.buildmart.data.datasource.LocalDataSourceChat
import com.versaiilers.buildmart.data.datasource.RemoteDataSourceChat
import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Message
import com.versaiilers.buildmart.domain.repository.ChatRepository
import java.util.Stack
import javax.inject.*

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSourceChat,
    private val remoteDataSource: RemoteDataSourceChat
) : ChatRepository {

    companion object {
        private const val TAG = "ChatRepositoryImpl"
    }

    // Получаем локальные чаты сначала
    override suspend fun getAllChatsLocal(userGlobalId: String): Result<List<Chat>> {
        val localChats = localDataSource.getChats()
        return if (localChats.isNotEmpty()) {
            Result.success(localChats)  // Сразу возвращаем локальные чаты
        } else {
            Log.d(TAG, "Локальные чаты пусты, загружаем с сервера")
            refreshChats(userGlobalId)  // Загружаем с сервера и обновляем локальное хранилище
        }
    }

    override suspend fun getAllChatsRemote(userGlobalId: String): Result<List<Chat>> {
        TODO("Not yet implemented")
    }

    // Получаем серверные чаты и обновляем локальное хранилище
    suspend fun refreshChats(userGlobalId: String): Result<List<Chat>> {
        return try {
            remoteDataSource.getAllChats(userGlobalId).also { result ->
                result.getOrNull()?.let { (chats, messages) ->
                    saveToLocal(chats, messages)
                }
            }.map { it.first }  // Возвращаем только список чатов
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка обновления чатов: ${e.message}")
            Result.failure(e)
        }
    }

    private fun saveToLocal(chats: List<Chat>, messages: List<List<Message>>) {
        localDataSource.saveChats(chats)
        localDataSource.saveMessages(messages)
        Log.d(TAG, "Данные успешно сохранены в локальное хранилище")
    }

    // ChatRepositoryImpl.kt
    fun connectWebSocket(onNewMessage: (Message) -> Unit) {
        remoteDataSource.connectWebSocket { message ->
            // Сохраняем новое сообщение в локальное хранилище
            localDataSource.saveMessages(listOf(message))

            // Передаем новое сообщение дальше для обработки в ViewModel или фрагменте
            onNewMessage(message)
        }
    }

    // Отключение от WebSocket
    fun disconnectWebSocket(): Result<Unit> {
        return try {
            remoteDataSource.disconnectWebSocket()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка отключения WebSocket: ${e.message}")
            Result.failure(e)
        }
    }
}