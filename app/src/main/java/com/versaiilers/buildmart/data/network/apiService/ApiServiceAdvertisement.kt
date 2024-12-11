package com.versaiilers.buildmart.data.network.apiService

import com.versaiilers.buildmart.data.network.ApiResponse
import retrofit2.http.GET

interface ApiServiceAdvertisement {
    @GET("/ad/getAll/")
    suspend fun getAll():ApiResponse
}