package com.versaiilers.buildmart.domain.useCase

import com.versaiilers.buildmart.data.repository.ChatRepositoryImpl
import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Message
import jakarta.inject.Inject

class ChatUseCase @Inject constructor(
    private val chatRepositoryImpl: ChatRepositoryImpl){

    suspend fun getAllChatsLocal(userGlobalId:String):Result<List<Chat>>{
        return chatRepositoryImpl.getAllChatsLocal(userGlobalId)
    }
    suspend fun refreshChats(userGlobalId:String):Result<List<Chat>>{
        return chatRepositoryImpl.refreshChats(userGlobalId)
    }
    // Подключение к WebSocket для получения новых сообщений
    fun connectToWebSocket(onNewMessage: (Message) -> Unit) {
        chatRepositoryImpl.connectWebSocket { message ->
            onNewMessage(message)  // Перенаправляем сообщение обратно в UI или другую часть системы
        }
    }

    // Отключение от WebSocket
    fun disconnectWebSocket(): Result<Unit> {
        return chatRepositoryImpl.disconnectWebSocket()
    }
}