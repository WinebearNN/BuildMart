package com.versaiilers.buildmart.data.datasource

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.versaiilers.buildmart.data.network.ApiServiceUser
import com.versaiilers.buildmart.data.network.RegisterUserRequest
import com.versaiilers.buildmart.domain.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RemoteDataSourceUser @Inject constructor(
    private val apiServiceUser: ApiServiceUser
) {
    private val gson: Gson = GsonBuilder()
        .setDateFormat(SimpleDateFormat("MMM d, yyyy", Locale("ru", "RU")).toPattern())
        .create()

    companion object {
        private const val TAG = "RemoteDataSourceUser"
    }

    suspend fun registerUser(user: User): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val request = RegisterUserRequest(
                email = user.email,
                password = user.password,
                phoneNumber = user.phoneNumber
            )
            val response = apiServiceUser.registerUser(request)
            if (response.success) response.message else throw Exception("Registration failed: ${response.message}")
        }.onFailure { e ->
            Log.e(TAG, "An error occurred during registration", e)
        }
    }

    suspend fun getUserByEmail(user: User): Result<User> = withContext(Dispatchers.IO) {
        runCatching {
            val response = apiServiceUser.getUserByEmail(user.email)
            if (response.success) {
                val userResponse = gson.fromJson(response.message, User::class.java)
                if (user.password == userResponse.password) userResponse
                else throw Exception("Invalid password")
            } else {
                throw Exception("Failed to get user: ${response.message}")
            }
        }.onSuccess {
            Log.d(TAG, "User fetched successfully: ${it.email}")
        }.onFailure { e ->
            Log.e(TAG, "An error occurred while fetching the user", e)
        }
    }
}