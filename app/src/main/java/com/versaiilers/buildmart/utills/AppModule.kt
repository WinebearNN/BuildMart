package com.versaiilers.buildmart.utills

import com.versaiilers.buildmart.data.datasource.user.RemoteDataSourceUser
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
import com.versaiilers.buildmart.data.datasource.chat.LocalDataSourceChat
import com.versaiilers.buildmart.data.datasource.chat.RemoteDataSourceChat
import com.versaiilers.buildmart.data.datasource.message.LocalDataSourceMessage
import com.versaiilers.buildmart.data.datasource.message.RemoteDataSourceMessage
import com.versaiilers.buildmart.data.datasource.user.LocalDataSourceUser
import com.versaiilers.buildmart.data.network.ApiServiceChat
import com.versaiilers.buildmart.data.network.ApiServiceMessage
import com.versaiilers.buildmart.data.network.WebSocketConnection
import com.versaiilers.buildmart.data.repository.ChatMessageRepositoryImpl
import com.versaiilers.buildmart.domain.repository.ChatMessageRepository
import com.versaiilers.buildmart.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideWebSocketConnection(okHttpClient: OkHttpClient): WebSocketConnection {
        return WebSocketConnection(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSourceChat(
        apiServiceChat: ApiServiceChat,
        webSocketConnection: WebSocketConnection
    ): RemoteDataSourceChat {
        return RemoteDataSourceChat(apiServiceChat, webSocketConnection)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSourceMessage(
        apiServiceMessage: ApiServiceMessage,
        webSocketConnection: WebSocketConnection
    ): RemoteDataSourceMessage {
        return RemoteDataSourceMessage(apiServiceMessage, webSocketConnection)
    }


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
    fun provideApiServiceMessage(): ApiServiceMessage {
        return ApiServiceProvider.apiServiceMessage
    }


    @Provides
    @Singleton
    fun provideRemoteDataSourceUser(apiServiceUser: ApiServiceUser): RemoteDataSourceUser {
        return RemoteDataSourceUser(apiServiceUser)  // Предоставляем RemoteDataSource
    }

    @Provides
    @Singleton
    fun provideLocalDataSourceUser(boxStore: BoxStore): LocalDataSourceUser {
        return LocalDataSourceUser(boxStore)
    }


    @Provides
    @Singleton
    fun provideLocalDataSourceMessage(boxStore: BoxStore): LocalDataSourceMessage {
        return LocalDataSourceMessage(boxStore)
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
        localDataSourceUser: LocalDataSourceUser
    ): UserRepository { // Изменено на UserRepository
        return UserRepositoryImpl(
            remoteDataSourceUser,
            localDataSourceUser
        )  // Предоставляем UserRepositoryImpl как UserRepository
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        remoteDataSourceChat: RemoteDataSourceChat,
        localDataSourceChat: LocalDataSourceChat,
        localDataSourceMessage: LocalDataSourceMessage,
        remoteDataSourceMessage: RemoteDataSourceMessage
    ): ChatMessageRepository {
        return ChatMessageRepositoryImpl(
            localDataSourceChat,
            remoteDataSourceChat,
            localDataSourceMessage,
            remoteDataSourceMessage
        )
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