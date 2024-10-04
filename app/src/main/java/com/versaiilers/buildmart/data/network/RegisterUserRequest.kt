package com.versaiilers.buildmart.data.network

data class RegisterUserRequest(
    val email: String,
    val password: String,
    val phoneNumber: String
)

data class ApiResponse(
    val success: Boolean,
    val message: String
)
