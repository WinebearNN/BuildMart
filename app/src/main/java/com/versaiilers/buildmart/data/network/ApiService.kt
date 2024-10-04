package com.versaiilers.buildmart.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/user/registration/")
    suspend fun registerUser(@Body request: RegisterUserRequest): ApiResponse

    @GET("user/get/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): ApiResponse

}