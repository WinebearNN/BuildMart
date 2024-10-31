package com.versaiilers.buildmart.data.network

import com.versaiilers.buildmart.domain.entity.Chat
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceChat {
    @POST("/chat/create/")
    suspend fun createChat(@Body request:Chat):ApiResponse

    @GET("/chat/get/{globalId}")
    suspend fun getChatByUserGlobalId(@Path("globalId") globalId:String):ApiResponse
}