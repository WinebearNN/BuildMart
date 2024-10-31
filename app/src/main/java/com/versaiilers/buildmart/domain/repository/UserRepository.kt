package com.versaiilers.buildmart.domain.repository


import com.versaiilers.buildmart.domain.entity.User


interface UserRepository {
    suspend fun register(user: User): Result<Unit>
    suspend fun loginUserByEmail(user: User): Result<User>
    suspend fun authUser(): Result<User>

}
