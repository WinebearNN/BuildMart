package com.versaiilers.buildmart.data.network.apiService

import com.versaiilers.buildmart.data.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceMessage {
    @GET("/message/get/{chatGlobalId}")
    suspend fun getMessagesByChatGlobalId(@Path("chatGlobalId") chatGlobalId:Long): ApiResponse

}