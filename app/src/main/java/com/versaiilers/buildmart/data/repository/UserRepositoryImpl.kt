package com.versaiilers.buildmart.data.repository


import android.util.Log
import com.versaiilers.buildmart.data.datasource.RemoteDataSourceUser
import com.versaiilers.buildmart.domain.entity.User
import com.versaiilers.buildmart.domain.repository.UserRepository
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSourceUser: RemoteDataSourceUser,
    boxStore: BoxStore
) : UserRepository {

    private val userBox: Box<User> = boxStore.boxFor(User::class.java)

    override suspend fun register(user: User): Result<Unit> {
        val result = remoteDataSourceUser.registerUser(user)
        if (result.isSuccess) {
            user.globalId=result.getOrNull()!!.toLong()
            userBox.put(user)  // Сохраняем пользователя в локальном хранилище
            return Result.success(Unit)
        }
        return Result.failure(Exception(result.getOrNull()))
    }

    override suspend fun loginUserByEmail(user: User): Result<User> {
        val result = remoteDataSourceUser.getUserByEmail(user)
        if (result.isSuccess) {
            result.getOrNull()
                ?.let { userBox.put(it) }  // Сохраняем пользователя в локальном хранилище
        }
        return result
    }

    override suspend fun authUser(): Result<User> {
        try {
//            userBox.remove(1)
//            userBox.remove(2)
//            userBox.remove(3)


            val result = userBox.contains(1)
            Log.d("UserRepositoryImpl",userBox.all.toString())
            if (result) {
                Log.d("UserRepositoryImpl",userBox.get(1).toString())
                return Result.success(userBox.get(1))
            }
            return Result.failure(Exception("User not auth before"))
        }catch (e:Exception) {
            return Result.failure(Exception("User not auth before ${e.message}"))
        }
    }
}