package com.versaiilers.buildmart.domain.repository

import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Message

interface ChatMessageRepository {
    suspend fun getAllChatsLocal(userGlobalId:String): Result<List<Chat>>
    suspend fun getAllChatsRemote(userGlobalId:String):Result<List<Chat>>
    suspend fun getAllMessagesLocal(chatGlobalId: Long):Result<List<Message>>
    suspend fun saveMessageLocal(message:Message)
    suspend fun refreshChats(userGlobalId: String): Result<List<Chat>>
    suspend fun sendMessageOverWebSocket(message: Message):Result<Unit>
}