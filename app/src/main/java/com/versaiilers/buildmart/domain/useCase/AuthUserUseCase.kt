package com.versaiilers.buildmart.domain.useCase

import com.versaiilers.buildmart.domain.entity.User
import com.versaiilers.buildmart.domain.repository.UserRepository
import javax.inject.Inject

class AuthUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    // Выполняем логику регистрации пользователя
    suspend fun execute(): Result<User> {
        // Регистрируем пользователя через репозиторий
        return userRepository.authUser()
    }



}