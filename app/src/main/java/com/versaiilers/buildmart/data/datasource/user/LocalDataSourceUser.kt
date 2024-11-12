package com.versaiilers.buildmart.data.datasource.user

import com.versaiilers.buildmart.domain.entity.User
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Inject

class LocalDataSourceUser @Inject constructor(
    boxStore: BoxStore
) {
    private val userBox: Box<User>  = boxStore.boxFor(User::class.java)

    companion object {
        private const val TAG = "LocalDataSourceUser"
    }

    fun saveUser(user:User){
        userBox.put(user)
    }

    fun getUser(id:Long):User{
        return userBox.get(id)
    }

    fun contains(id:Long):Boolean{
        return userBox.contains(id)
    }




}