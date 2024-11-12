package com.versaiilers.buildmart.data.repository


import android.util.Log
import com.versaiilers.buildmart.data.datasource.user.LocalDataSourceUser
import com.versaiilers.buildmart.data.datasource.user.RemoteDataSourceUser
import com.versaiilers.buildmart.domain.entity.User
import com.versaiilers.buildmart.domain.repository.UserRepository
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSourceUser: RemoteDataSourceUser,
    private val localDataSourceUser: LocalDataSourceUser
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }


    override suspend fun register(user: User): Result<Unit> {
        val result = remoteDataSourceUser.registerUser(user)
        if (result.isSuccess) {
            user.globalId=result.getOrNull()!!.toLong()
            localDataSourceUser.saveUser(user)  // Сохраняем пользователя в локальном хранилище
            return Result.success(Unit)
        }
        return Result.failure(Exception(result.getOrNull()))
    }

    override suspend fun loginUserByEmail(user: User): Result<User> {
        val result = remoteDataSourceUser.getUserByEmail(user)
        if (result.isSuccess) {
            result.getOrNull()
                ?.let { localDataSourceUser.saveUser(it) }  // Сохраняем пользователя в локальном хранилище
        }
        return result
    }

    override suspend fun authUser(): Result<User> {
        try {
//            userBox.remove(1)
//            userBox.remove(2)
//            userBox.remove(3)



//            Log.d("UserRepositoryImpl",userBox.all.toString())
            if (localDataSourceUser.contains(1)) {
                val userFinal=localDataSourceUser.getUser(1)
                Log.d("UserRepositoryImpl",userFinal.toString())
                return Result.success(userFinal)
            }
            return Result.failure(Exception("User not auth before"))
        }catch (e:Exception) {
            return Result.failure(Exception("User not auth before ${e.message}"))
        }
    }
}