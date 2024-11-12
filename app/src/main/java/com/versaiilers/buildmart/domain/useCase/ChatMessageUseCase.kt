package com.versaiilers.buildmart.domain.useCase

import com.versaiilers.buildmart.data.repository.ChatMessageRepositoryImpl
import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Message
import jakarta.inject.Inject

class ChatMessageUseCase @Inject constructor(
    private val chatMessageRepositoryImpl: ChatMessageRepositoryImpl){

    suspend fun getAllChatsLocal(userGlobalId:String):Result<List<Chat>>{
        return chatMessageRepositoryImpl.getAllChatsLocal(userGlobalId)
    }


    suspend fun refreshChats(userGlobalId:String):Result<List<Chat>>{
        return chatMessageRepositoryImpl.refreshChats(userGlobalId)
    }

    suspend fun getAllMessagesLocal(chatGlobalId:Long):Result<List<Message>>{
        return chatMessageRepositoryImpl.getAllMessagesLocal(chatGlobalId)
    }

    suspend fun saveMessageLocal(message:Message){
        return chatMessageRepositoryImpl.saveMessageLocal(message)
    }

    suspend fun refreshMessages(chatGlobalId: Long): Result<List<Message>> {
        return chatMessageRepositoryImpl.refreshMessages(chatGlobalId)
    }

    suspend fun sendMessageOverWebSocket(message: Message): Result<Unit>{
        return chatMessageRepositoryImpl.sendMessageOverWebSocket(message)
    }


    // Подключение к WebSocket для получения новых сообщений
    fun connectToWebSocket(onNewMessage: (Message) -> Unit) {
        chatMessageRepositoryImpl.connectWebSocket { message ->
            onNewMessage(message)  // Перенаправляем сообщение обратно в UI или другую часть системы
        }
    }

    // Отключение от WebSocket
    fun disconnectWebSocket(): Result<Unit> {
        return chatMessageRepositoryImpl.disconnectWebSocket()
    }


}