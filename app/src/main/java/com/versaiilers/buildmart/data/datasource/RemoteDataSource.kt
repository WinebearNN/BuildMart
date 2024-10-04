package com.versaiilers.buildmart.data.datasource

import android.util.Log
import com.google.gson.GsonBuilder
import com.versaiilers.buildmart.Utills.ErrorCode
import com.versaiilers.buildmart.data.network.ApiService
import com.versaiilers.buildmart.data.network.RegisterUserRequest
import com.versaiilers.buildmart.domain.entity.User
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun registerUser(user: User): Result<Unit> {
        return try {
            val request = RegisterUserRequest(
                email = user.email,
                password = user.password,
                phoneNumber = user.phoneNumber
            )
            val response = apiService.registerUser(request)

            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Registration failed: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("An error occurred during registration: ${e.localizedMessage}", e))
        }
    }

    suspend fun getUserByEmail(user: User): Result<User> {
        // Указываем формат даты
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale("ru", "RU"))

        // Создаем Gson с кастомным форматом для дат
        val gson = GsonBuilder()
            .setDateFormat(dateFormat.toPattern())
            .create()

        return try {
            val response = apiService.getUserByEmail(user.email)

            if (response.success) {
                Log.d("RemoteDataSource", "User fetched successfully: ${response.message}")
                val userResponse = gson.fromJson(response.message, User::class.java)

                // Сравнение паролей
                if (user.password == userResponse.password) {
                    Result.success(userResponse)
                } else {
                    Result.failure(Exception("Invalid password"))
                }
            } else {
                Result.failure(Exception("Failed to get user: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("An error occurred while fetching the user: ${e.localizedMessage}", e))
        }
    }
}