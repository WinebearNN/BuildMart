package com.versaiilers.buildmart.Utills

import com.versaiilers.buildmart.data.datasource.RemoteDataSource
import com.versaiilers.buildmart.data.network.ApiService
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
import com.versaiilers.buildmart.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiServiceProvider.apiService  // Метод создания вашего ApiService
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSource(apiService)  // Предоставляем RemoteDataSource
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        remoteDataSource: RemoteDataSource,
        boxStore: BoxStore
    ): UserRepository { // Изменено на UserRepository
        return UserRepositoryImpl(remoteDataSource, boxStore)  // Предоставляем UserRepositoryImpl как UserRepository
    }

    @Provides
    @Singleton
    fun provideBoxStore(@ApplicationContext context: Context): BoxStore {
        return MyObjectBox.builder().androidContext(context).build()  // Создание BoxStore с использованием контекста приложения
    }
}