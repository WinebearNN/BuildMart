package com.versaiilers.buildmart.domain.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
data class Chat(
    @Id
    var id: Long = 0,
    @Unique
    var globalId: Long = 0,
    var participantFirstName: String = "",
    var participantFirstGlobalId: Long = 0,
    var participantSecondName: String = "",
    var participantSecondGlobalId: Long = 0,
    var lastMessage: String=""
)



@Entity
data class Message(
    @Id
    var id: Long = 0,
    @Unique
    var globalId: Long=0,
    var userGlobalId: Long = 0,
    var chatGlobalId: Long = 0,
    val content: String = "",
    val timestamp: Long = 0
)


