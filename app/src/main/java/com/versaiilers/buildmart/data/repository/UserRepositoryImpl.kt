package com.versaiilers.buildmart.data.repository


import com.versaiilers.buildmart.data.datasource.RemoteDataSource
import com.versaiilers.buildmart.domain.entity.User
import com.versaiilers.buildmart.domain.repository.UserRepository
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val boxStore: BoxStore
) : UserRepository {

    private val userBox: Box<User> = boxStore.boxFor(User::class.java)

    override suspend fun register(user: User): Result<Unit> {
        val result = remoteDataSource.registerUser(user)
        if (result.isSuccess) {
            userBox.put(user)  // Сохраняем пользователя в локальном хранилище
        }
        return result
    }

    override suspend fun loginUserByEmail(user: User): Result<User> {
        val result = remoteDataSource.getUserByEmail(user)
        if (result.isSuccess) {
            result.getOrNull()?.let { userBox.put(it) }  // Сохраняем пользователя в локальном хранилище
        }
        return result
    }
}