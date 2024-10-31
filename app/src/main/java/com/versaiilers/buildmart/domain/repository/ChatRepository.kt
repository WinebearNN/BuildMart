package com.versaiilers.buildmart.domain.repository

import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.User

interface ChatRepository {
    suspend fun getAllChatsLocal(userGlobalId:String): Result<List<Chat>>
    suspend fun getAllChatsRemote(userGlobalId:String):Result<List<Chat>>
}