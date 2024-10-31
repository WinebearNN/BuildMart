package com.versaiilers.buildmart.utills

import com.versaiilers.buildmart.data.datasource.RemoteDataSourceUser
import com.versaiilers.buildmart.data.network.ApiServiceUser
import com.versaiilers.buildmart.data.network.ApiServiceProvider
import com.versaiilers.buildmart.data.repository.UserRepositoryImpl
import com.versaiilers.buildmart.domain.entity.MyObjectBox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.BoxStore
import javax.inject.Singleton
import android.content.Context
import android.util.Log
import com.getkeepsafe.relinker.ReLinker
import com.versaiilers.buildmart.data.datasource.LocalDataSourceChat
import com.versaiilers.buildmart.data.datasource.RemoteDataSourceChat
import com.versaiilers.buildmart.data.network.ApiServiceChat
import com.versaiilers.buildmart.data.repository.ChatRepositoryImpl
import com.versaiilers.buildmart.domain.repository.ChatRepository
import com.versaiilers.buildmart.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.WebSocket

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiServiceUser(): ApiServiceUser {
        return ApiServiceProvider.apiServiceUser  // Метод создания вашего ApiService
    }

    @Provides
    @Singleton
    fun provideApiServiceChat(): ApiServiceChat {
        return ApiServiceProvider.apiServiceChat  // Метод создания вашего ApiService
    }

    @Provides
    @Singleton
    fun provideWebSocketClient(): OkHttpClient{
        return ApiServiceProvider.webSocketClient
    }

    @Provides
    @Singleton
    fun provideRemoteDataSourceUser(apiServiceUser: ApiServiceUser): RemoteDataSourceUser {
        return RemoteDataSourceUser(apiServiceUser)  // Предоставляем RemoteDataSource
    }
    @Provides
    @Singleton
    fun provideRemoteDataSourceChat(apiServiceChat: ApiServiceChat,webSocketClient: OkHttpClient): RemoteDataSourceChat {
        return RemoteDataSourceChat(apiServiceChat,webSocketClient) // Предоставляем RemoteDataSource
    }

    @Provides
    @Singleton
    fun provideLocalDataSourceChat(boxStore: BoxStore): LocalDataSourceChat {
        return LocalDataSourceChat(boxStore) // Предоставляем RemoteDataSource
    }


    @Provides
    @Singleton
    fun provideUserRepository(
        remoteDataSourceUser: RemoteDataSourceUser,
        boxStore: BoxStore
    ): UserRepository { // Изменено на UserRepository
        return UserRepositoryImpl(remoteDataSourceUser, boxStore)  // Предоставляем UserRepositoryImpl как UserRepository
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        remoteDataSourceChat: RemoteDataSourceChat,
        localDataSourceChat: LocalDataSourceChat
    ): ChatRepository{
        return ChatRepositoryImpl(localDataSourceChat,remoteDataSourceChat)
    }

    @Provides
    @Singleton
    fun provideBoxStore(@ApplicationContext context: Context): BoxStore {
        val boxStore = MyObjectBox.builder()
            .androidContext(context)
            .androidReLinker(ReLinker.log(object : ReLinker.Logger {
                override fun log(message: String) {
                    Log.d("ObjectBoxLog", message)
                }
            }))
            .build()

//        boxStore.close()  // Закрываем существующее подключение
//        boxStore.deleteAllFiles()  // Удаляем все файлы базы данных

        return boxStore;


    }
}