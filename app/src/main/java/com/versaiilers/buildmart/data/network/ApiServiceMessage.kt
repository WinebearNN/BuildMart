package com.versaiilers.buildmart.data.network

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceMessage {
    @GET("/message/get/{chatGlobalId}")
    suspend fun getMessagesByChatGlobalId(@Path("chatGlobalId") chatGlobalId:Long):ApiResponse

}